## MongoDB Advanced Assignment Answers

## Multiple Choice Questions

Q1: B
Q2: B
Q3: C
Q4: A
Q5: C
Q6: B
Q7: C
Q8: B*
Q9: C
Q10: B
Q11: A
Q12: C*
---

## Task 5.2 – Explain Execution Statistics

**Query executed:**

```javascript
db.employees.find({
  departmentId: 10
}).sort({
  name: 1
}).explain("executionStats");
```

**Observed results from the executed query:**

Winning plan - FETCH 
Input stage - IXSCAN 
Index used - `departmentId_1_name_1` 
Documents returned (`nReturned`) - 2
Keys examined (`totalKeysExamined`) - 2 
Documents examined (`totalDocsExamined`) - 2 

### Explanation

- **IXSCAN:** MongoDB used the compound index to find matching records.
- **FETCH:** MongoDB retrieved the complete matching documents.
- **nReturned:** Number of documents returned by the query.
- **totalKeysExamined:** Number of index keys examined.
- **totalDocsExamined:** Number of documents examined.
- **executionTimeMillis:** Time taken to execute the query in milliseconds.