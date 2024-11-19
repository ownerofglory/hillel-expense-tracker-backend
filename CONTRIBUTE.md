# Contributing Guidelines

Thank you for contributing to our project! Please adhere to the following guidelines to maintain code quality and consistency across the repository.

---

## Commit and Branch Requirements

### Branches
- **`main` branch**:
  - Contains production-ready code. No direct commits are allowed here.
- **`feature/*` sub-branches**:
  - Used for developing new features.
  - Must include the issue ID and a brief title.
  - Example: `feature/100200_add-expense-creation`.
- **`bugfix/*` sub-branches**:
  - Used for fixing bugs.
  - Must include the issue ID and a brief title.
  - Example: `bugfix/20101_fix-missing-info`.

---

### Commits
- **Multiline commits are accepted.**
- The **first line** of the commit must:
  - Contain the issue ID.
  - Specify the type: `[FEATURE]` or `[BUG]`.
  - Include a short, descriptive title.
  - Be followed by a line break.
- Subsequent lines should:
  - Use bullet points (`-`) to briefly describe the changes.

#### **Commit Example:**
```
480100: [FEATURE] Add database connection

- database driver added
- added queries code
```

---

## Pull Requests
- **Required Reviewer**:
  - All pull requests must include `@ownerofglory` as a required reviewer.
- **Optional Reviewers**:
  - One or more optional reviewers can be added as needed.
- **Pipeline**:
  - All pull requests must pass the pipeline successfully.

---

## Definition of 'Done' (DoD)

### **Code Quality and Functionality**
- Code changes must:
  - Be thoroughly reviewed and free of new bugs.
  - Compile and build without errors.
  - Align with the project's design and architecture.
  - Fulfill all functional requirements.

### **Testing**
- Code changes must:
  - Be covered by unit tests.
  - Pass all unit tests (green status).

### **Documentation**
- Relevant documentation must:
  - Be updated to reflect any changes in the codebase.

---

By following these guidelines, you help us maintain a high-quality codebase and streamline the development process. Thank you for your cooperation and contributions!
