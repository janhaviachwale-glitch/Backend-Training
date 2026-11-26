# MongoDB Basics

## Overview

This repository contains my practice work with MongoDB. I used MongoDB Community Server and MongoDB Shell (`mongosh`) to learn and practise basic MongoDB operations.

## Tools Used

- MongoDB Community Server
- MongoDB Shell (`mongosh`)
- JavaScript

## What I Learned and Practised

### 1. Database Setup
- Installed and started MongoDB locally.
- Connected to MongoDB using `mongosh`.
- Created a database named `company`.
- Created an `employees` collection.
- Inserted five employee records.

### 2. Reading Data
- Retrieved employees based on their department.
- Filtered employees based on experience.
- Queried nested fields such as city.
- Searched for employees with specific skills.
- Used sorting and limits to get the required results.

### 3. Updating Data
- Used `$inc` to increase an employee's experience.
- Used `$set` to update an employee's active status.
- Used `$addToSet` to add a skill without creating duplicates.

### 4. Deleting Data
- Inserted a temporary employee record.
- Deleted the record using `deleteOne()`.
- Checked that the record was deleted successfully.

### 5. Indexing
- Checked the existing indexes.
- Created an ascending index on the `department` field.
- Verified the indexes using `getIndexes()`.

### 6. Aggregation
- Counted employees in each department.
- Calculated the average experience in each department.
- Retrieved the two most experienced employees from Engineering.

## Files

| File | Description |
|---|---|
| `mongodb-basic-assignment.js` | MongoDB commands and queries |
| `answers.md` | Answers to MongoDB questions |

## How to Run

1. Start the MongoDB server:

   ```bash
   mongod --dbpath /usr/local/var/mongodb
   ```

2. Open another terminal and connect using:

   ```bash
   mongosh
   ```

3. Run the commands from the JavaScript file in MongoDB Shell.