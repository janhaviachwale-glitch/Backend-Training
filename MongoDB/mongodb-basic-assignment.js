
// ==================================================
// MongoDB Basic Assignment
// ==================================================


// ==================================================
// TASK 1: LOCAL SETUP AND DATA CREATION
// ==================================================

// Step 1: Verify MongoDB connection
// Run mongosh in the terminal before executing this file.
show dbs;

// Step 2: Select the company database
use company;

// Step 3: Insert the employee dataset
db.employees.insertMany([
    {
        _id: "EMP001",
        name: "John",
        department: "Engineering",
        experience: 4,
        skills: ["Java", "Spring Boot"],
        active: true,
        address: {
            city: "Pune",
            country: "India"
        }
    },
    {
        _id: "EMP002",
        name: "Alice",
        department: "HR",
        experience: 3,
        skills: ["Recruitment", "Communication"],
        active: true,
        address: {
            city: "Mumbai",
            country: "India"
        }
    },
    {
        _id: "EMP003",
        name: "David",
        department: "Engineering",
        experience: 6,
        skills: ["Java", "MongoDB"],
        active: true,
        address: {
            city: "Bengaluru",
            country: "India"
        }
    },
    {
        _id: "EMP004",
        name: "Emma",
        department: "Finance",
        experience: 2,
        skills: ["Accounting", "Excel"],
        active: false,
        address: {
            city: "Pune",
            country: "India"
        }
    },
    {
        _id: "EMP005",
        name: "Robert",
        department: "Engineering",
        experience: 5,
        skills: ["Java", "Docker"],
        active: true,
        address: {
            city: "Delhi",
            country: "India"
        }
    }
]);

// Verify the database and collection
show dbs;
show collections;

// Verify that the collection contains 5 documents
db.employees.countDocuments();


// ==================================================
// TASK 2: READ AND QUERY OPERATIONS
// ==================================================

// 2.1 Engineering Employees
// Retrieve Engineering employees.
// Return _id, name and experience.
// Sort by name in ascending order.

db.employees.find(
    { department: "Engineering" },
    {
        _id: 1,
        name: 1,
        experience: 1
    }
).sort({ name: 1 });


// 2.2 Employees with 5 or More Years of Experience
// Return name and experience.
// Exclude _id and sort by experience descending.

db.employees.find(
    { experience: { $gte: 5 } },
    {
        _id: 0,
        name: 1,
        experience: 1
    }
).sort({ experience: -1 });


// 2.3 Nested Document Query
// Retrieve employees whose city is Pune.
// Return name and city, sorted by name.

db.employees.find(
    { "address.city": "Pune" },
    {
        _id: 0,
        name: 1,
        "address.city": 1
    }
).sort({ name: 1 });


// 2.4 Array Query
// Find employees whose skills contain MongoDB or Spring Boot.
// Sort by name in ascending order.

db.employees.find(
    { skills: { $in: ["MongoDB", "Spring Boot"] } }
).sort({ name: 1 });


// 2.5 Sorting and Limiting
// Retrieve the top 2 employees by experience.
// Return name and experience.

db.employees.find(
    {},
    {
        _id: 0,
        name: 1,
        experience: 1
    }
).sort({ experience: -1 }).limit(2);


// ==================================================
// TASK 3: UPDATE OPERATIONS
// ==================================================

// 3.1 $inc
// Increase Alice's experience by 1.

db.employees.updateOne(
    { name: "Alice" },
    { $inc: { experience: 1 } }
);


// 3.2 $set
// Change Emma's active value to true.

db.employees.updateOne(
    { name: "Emma" },
    { $set: { active: true } }
);


// 3.3 $addToSet
// Add MongoDB to Robert's skills array.

db.employees.updateOne(
    { name: "Robert" },
    { $addToSet: { skills: "MongoDB" } }
);

// Execute the same operation again to verify
// that MongoDB is not added as a duplicate.

db.employees.updateOne(
    { name: "Robert" },
    { $addToSet: { skills: "MongoDB" } }
);


// ==================================================
// TASK 4: DELETE OPERATION
// ==================================================

// Insert the temporary employee document.

db.employees.insertOne({
    _id: "EMP999",
    name: "Temporary Employee",
    department: "Training",
    experience: 0
});

// Delete only the temporary employee.

db.employees.deleteOne({
    _id: "EMP999"
});

// Verify that the temporary employee was deleted.
// Expected result: null

db.employees.findOne({
    _id: "EMP999"
});

// Verify that the original five documents remain.

db.employees.countDocuments();


// ==================================================
// TASK 5: INDEXING
// ==================================================

// 5.1 Check existing indexes
// Identify the automatically created _id_ index.

db.employees.getIndexes();

// 5.2 Create an ascending index on department.

db.employees.createIndex({
    department: 1
});

// 5.3 Verify the indexes.
// Expected indexes: _id_ and department_1

db.employees.getIndexes();


// ==================================================
// TASK 6: AGGREGATION
// ==================================================

// 6.1 Employees per Department
// Count employees in each department.
// Sort by department name.

db.employees.aggregate([
    {
        $group: {
            _id: "$department",
            totalEmployees: { $sum: 1 }
        }
    },
    {
        $sort: { _id: 1 }
    }
]);


// 6.2 Average Experience per Department
// Calculate average experience for each department.
// Sort by department name in ascending order.

db.employees.aggregate([
    {
        $group: {
            _id: "$department",
            averageExperience: { $avg: "$experience" }
        }
    },
    {
        $sort: { _id: 1 }
    }
]);


// 6.3 Top Engineering Employees
// Select Engineering employees.
// Return name and experience, excluding _id.
// Sort by experience descending and limit to 2.

db.employees.aggregate([
    {
        $match: {
            department: "Engineering"
        }
    },
    {
        $project: {
            _id: 0,
            name: 1,
            experience: 1
        }
    },
    {
        $sort: {
            experience: -1
        }
    },
    {
        $limit: 2
    }
]);