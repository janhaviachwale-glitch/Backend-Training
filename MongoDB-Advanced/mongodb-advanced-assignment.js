// MongoDB Advanced Assignment
// Database: company_advanced
// Tasks 1–5: commands used and verification commands

use("company_advanced");

// ------------------------------------------------------------
// Initial database and collection checks
// ------------------------------------------------------------

show dbs;
db;
show collections;

// ------------------------------------------------------------
// TASK 1 – Schema validation and sample data
// ------------------------------------------------------------

db.createCollection("employees", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["name", "departmentId", "experience", "active"],
      properties: {
        _id: { bsonType: "int" },
        name: { bsonType: "string" },
        departmentId: { bsonType: "int" },
        experience: { bsonType: "int" },
        active: { bsonType: "bool" },
        skills: {
          bsonType: "array",
          items: { bsonType: "string" }
        },
        certifications: {
          bsonType: "array",
          items: {
            bsonType: "object",
            required: ["name", "status", "expiryYear"],
            properties: {
              name: { bsonType: "string" },
              status: { bsonType: "string" },
              expiryYear: { bsonType: "int" }
            }
          }
        }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
});

// Insert the three valid employee documents.
db.employees.insertMany([
  {
    _id: 1,
    name: "John",
    departmentId: 10,
    skills: ["Java", "MongoDB"],
    experience: 4,
    active: true,
    certifications: [
      { name: "MongoDB", status: "Expired", expiryYear: 2026 }
    ]
  },
  {
    _id: 2,
    name: "Alice",
    departmentId: 10,
    skills: ["Python", "MongoDB"],
    experience: 6,
    active: true,
    certifications: [
      { name: "MongoDB", status: "Active", expiryYear: 2028 }
    ]
  },
  {
    _id: 3,
    name: "David",
    departmentId: 20,
    skills: ["Communication"],
    experience: 3,
    active: false,
    certifications: [
      { name: "Communication", status: "Active", expiryYear: 2027 }
    ]
  }
]);

// Verify inserted documents and count.
db.employees.find().pretty();
db.employees.countDocuments();

// Try inserting an invalid document.
// Expected: schema validation error; the document is rejected.
db.employees.insertOne({
  _id: 99,
  name: 123,
  departmentId: "HR",
  experience: "one",
  active: "yes"
});

// Verify that the invalid document was not inserted.
db.employees.countDocuments();

// ------------------------------------------------------------
// TASK 2 – Embedded documents and array operations
// ------------------------------------------------------------

// 2.1 Find employees with a MongoDB certification whose status is Active.
db.employees.find({
  certifications: {
    $elemMatch: {
      name: "MongoDB",
      status: "Active"
    }
  }
}).pretty();

// 2.2 Update John's MongoDB certification using the positional operator.
db.employees.updateOne(
  {
    name: "John",
    "certifications.name": "MongoDB"
  },
  {
    $set: { "certifications.$.status": "Active" }
  }
);

// Verify John's certification update.
db.employees.find(
  { name: "John" },
  { _id: 0, name: 1, certifications: 1 }
).pretty();

// 2.3 Update array elements with expiryYear less than 2027 using arrayFilters.
db.employees.updateMany(
  {},
  {
    $set: { "certifications.$[cert].status": "Renewal Due" }
  },
  {
    arrayFilters: [
      { "cert.expiryYear": { $lt: 2027 } }
    ]
  }
);

// Verify certification statuses.
db.employees.find(
  {},
  { _id: 0, name: 1, certifications: 1 }
).pretty();

// ------------------------------------------------------------
// TASK 3 – Bulk write operations
// ------------------------------------------------------------

db.employees.bulkWrite([
  {
    updateOne: {
      filter: { name: "John" },
      update: { $inc: { experience: 1 } }
    }
  },
  {
    updateOne: {
      filter: { name: "Alice" },
      update: { $set: { active: false } }
    }
  },
  {
    insertOne: {
      document: {
        _id: 4,
        name: "Emma",
        departmentId: 20,
        skills: ["Excel"],
        experience: 2,
        active: true,
        certifications: [
          { name: "Excel", status: "Active", expiryYear: 2026 }
        ]
      }
    }
  }
]);

// Verify bulk write results.
db.employees.find().sort({ _id: 1 }).pretty();
db.employees.countDocuments();

// ------------------------------------------------------------
// TASK 4 – Aggregation pipelines
// ------------------------------------------------------------

// Create the departments collection and insert department documents.
// We dropped it first to avoid duplicate _id values if recreating the data.
db.departments.drop();

db.departments.insertMany([
  { _id: 10, name: "Engineering" },
  { _id: 20, name: "HR" }
]);

// Verify department data.
db.departments.find().pretty();
db.departments.countDocuments();

// 4.1 Join employees with departments using $lookup.
db.employees.aggregate([
  {
    $lookup: {
      from: "departments",
      localField: "departmentId",
      foreignField: "_id",
      as: "department"
    }
  },
  { $unwind: "$department" },
  {
    $project: {
      _id: 0,
      employeeName: "$name",
      departmentName: "$department.name"
    }
  },
  { $sort: { employeeName: 1 } }
]);

// 4.2 Count employees for each skill.
db.employees.aggregate([
  { $unwind: "$skills" },
  {
    $group: {
      _id: "$skills",
      employeeCount: { $sum: 1 }
    }
  },
  { $sort: { employeeCount: -1, _id: 1 } }
]);

// 4.3 Use $facet to return active employee names and their total count.
db.employees.aggregate([
  { $match: { active: true } },
  {
    $facet: {
      activeEmployeeNames: [
        { $project: { _id: 0, name: 1 } },
        { $sort: { name: 1 } }
      ],
      totalActiveEmployees: [
        { $count: "count" }
      ]
    }
  }
]);

// ------------------------------------------------------------
// TASK 5 – Indexing and query optimization
// ------------------------------------------------------------

// 5.1 Create the compound index on departmentId and name.
db.employees.createIndex({
  departmentId: 1,
  name: 1
});

// Verify indexes.
db.employees.getIndexes();

// 5.2 View query execution statistics for department 10 sorted by name.
db.employees.find({
  departmentId: 10
}).sort({
  name: 1
}).explain("executionStats");

// 5.3 Create a sessions collection and insert a document with a future expiry.
db.sessions.drop();
db.createCollection("sessions");

db.sessions.insertOne({
  _id: 1,
  userName: "John",
  expiresAt: new Date(Date.now() + 600000)
});

// Create a TTL index that expires documents at the expiresAt date.
db.sessions.createIndex(
  { expiresAt: 1 },
  { expireAfterSeconds: 0 }
);

// Verify the TTL index and session document.
db.sessions.getIndexes();
db.sessions.find().pretty();

// ------------------------------------------------------------
// Final verification
// ------------------------------------------------------------

show collections;
db.employees.countDocuments();
db.departments.countDocuments();
db.sessions.countDocuments();
