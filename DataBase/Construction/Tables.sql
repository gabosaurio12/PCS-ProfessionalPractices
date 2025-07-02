xDROP DATABASE IF EXISTS ProfessionalPractices;
CREATE DATABASE ProfessionalPractices;

USE ProfessionalPractices;

CREATE TABLE Academic (
academicID INT PRIMARY KEY AUTO_INCREMENT,
personalNumber VARCHAR(10) UNIQUE NOT NULL,
name VARCHAR(100) NOT NULL,
paternalSurname VARCHAR(100) NOT NULL,
maternalSurname VARCHAR(100),
email VARCHAR(100) NOT NULL,
userName VARCHAR(100) NOT NULL,
password VARCHAR(100) NOT NULL,
role VARCHAR(20) NOT NULL
);

CREATE TABLE LinkedOrganization(
linkedOrganizationID INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(200) NOT NULL,
department VARCHAR(200) NOT NULL,
description VARCHAR(500) NOT NULL,
address VARCHAR(200) NOT NULL,
email VARCHAR(100) NOT NULL,
alterContact VARCHAR(100) NOT NULL
);

CREATE TABLE ProjectResponsible(
projectResponsibleID INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(150) NOT NULL,
email VARCHAR(100) NOT NULL,
alterContact VARCHAR(100) NOT NULL,
linkedOrganizationId INT,
FOREIGN KEY (linkedOrganizationId)
REFERENCES LinkedOrganization(linkedOrganizationID) ON UPDATE CASCADE
);

CREATE TABLE Project(
projectID INT PRIMARY KEY AUTO_INCREMENT,
title VARCHAR(150) NOT NULL,
category VARCHAR(100) NOT NULL,
beginningDate DATE NOT NULL,
endingDate DATE NOT NULL,
status VARCHAR(100) NOT NULL,
openSpots INT NOT NULL,
linkedOrganizationId INT,
projectResponsibleId INT NOT NULL,
FOREIGN KEY (linkedOrganizationId)
REFERENCES LinkedOrganization(linkedOrganizationId) ON UPDATE CASCADE ON DELETE SET NULL,
FOREIGN KEY (projectResponsibleId)
REFERENCES ProjectResponsible(projectResponsibleID) ON UPDATE CASCADE
);

CREATE TABLE Student(
studentID INT PRIMARY KEY AUTO_INCREMENT,
tuition VARCHAR(10) UNIQUE NOT NULL,
name VARCHAR(100) NOT NULL,
paternalSurname VARCHAR(100) NOT NULL,
maternalSurname VARCHAR(100),
email VARCHAR(100) NOT NULL,
userName VARCHAR(100) NOT NULL,
password VARCHAR(100) NOT NULL,
creditAdvance INT NOT NULL,
grade FLOAT NOT NULL,
academicId INT NOT NULL,
projectId INT,
FOREIGN KEY (academicID)
REFERENCES Academic(academicID) ON UPDATE CASCADE,
FOREIGN KEY (projectId)
REFERENCES Project(projectID) ON UPDATE CASCADE
);

CREATE TABLE Section(
sectionID INT PRIMARY KEY AUTO_INCREMENT,
nrc VARCHAR(15) NOT NULL,
period VARCHAR(10) NOT NULL
);

CREATE TABLE AcademicSection(
academicID INT,
sectionID INT,
PRIMARY KEY (academicID, sectionID),
FOREIGN KEY (academicID)
REFERENCES Academic(academicID) ON DELETE CASCADE,
FOREIGN KEY (sectionID)
REFERENCES Section(sectionID) ON DELETE CASCADE
);

CREATE TABLE StudentSection(
studentID INT,
sectionID INT,
PRIMARY KEY (studentID, sectionID),
FOREIGN KEY (studentID)
REFERENCES Student(studentID) ON DELETE CASCADE,
FOREIGN KEY (sectionID)
REFERENCES Section(sectionID) ON DELETE CASCADE
);

CREATE TABLE ProjectSection(
projectID INT,
sectionID INT,
PRIMARY KEY (projectId, sectionID),
FOREIGN KEY (projectId)
REFERENCES Project(projectID) ON DELETE CASCADE,
FOREIGN KEY (sectionID)
REFERENCES Section(sectionID) ON DELETE CASCADE
);

CREATE TABLE LinkedOrganizationSection(
linkedOrganizationID INT,
sectionID INT,
PRIMARY KEY (linkedOrganizationID, sectionID),
FOREIGN KEY (linkedOrganizationID)
REFERENCES LinkedOrganization(linkedOrganizationID) ON DELETE CASCADE,
FOREIGN KEY (sectionID)
REFERENCES Section(sectionID) ON DELETE CASCADE
);

CREATE TABLE ReportTypeCatalog(
reportTypeID INT PRIMARY KEY AUTO_INCREMENT,
type VARCHAR(50) NOT NULL
);

CREATE TABLE Report(
reportID INT PRIMARY KEY AUTO_INCREMENT,
activity VARCHAR(200) NOT NULL,
date DATE NOT NULL,
reportTypeId INT NOT NULL,
hours INT NOT NULL,
sectionID INT NOT NULL,
studentId INT NOT NULL,
reportPath VARCHAR(300) NOT NULL
);

CREATE TABLE ProjectAsignation(
asignationID INT PRIMARY KEY AUTO_INCREMENT,
asignationDocumentPath VARCHAR(500) NOT NULL,
studentId INT NOT NULL,
coordinatorId INT NOT NULL,
projectId INT NOT NULL,
FOREIGN KEY (coordinatorId)
REFERENCES Academic(academicID) ON UPDATE CASCADE,
FOREIGN KEY (projectId)
REFERENCES Project(projectID) ON UPDATE CASCADE
);

CREATE TABLE PresentationEvaluation(
presentationID INT PRIMARY KEY AUTO_INCREMENT,
presentationGrade FLOAT,
presentationPath VARCHAR(500) NOT NULL,
evaluatorId INT,
studentId INT NOT NULL,
FOREIGN KEY (evaluatorId)
REFERENCES Academic(academicID) ON UPDATE CASCADE,
FOREIGN KEY (studentId)
REFERENCES Student(studentID) ON UPDATE CASCADE
);

CREATE TABLE RolesCatalog(
roleID INT PRIMARY KEY AUTO_INCREMENT,
role INT NOT NULL
);

CREATE TABLE SystemConfiguration(
projectRegistrationStatus INT,
currentSectionID INT,
FOREIGN KEY (currentSectionID)
REFERENCES Section(sectionID) ON DELETE CASCADE
ON UPDATE CASCADE
);

CREATE TABLE StudentProgress(
progressID INT PRIMARY KEY AUTO_INCREMENT,
hoursValidated INT DEFAULT 0,
remainingHours INT DEFAULT 480,
tuition VARCHAR(10) NOT NULL,
FOREIGN KEY (tuition)
REFERENCES Student(tuition) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ProjectRequest (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
studentId INT NOT NULL,
documentPath VARCHAR(500) NOT NULL,
FOREIGN KEY (studentId) REFERENCES Student(studentID) ON UPDATE CASCADE
);

CREATE TABLE Presentation(
presentationID INT PRIMARY KEY AUTO_INCREMENT,
presentationGrade DOUBLE,
presentationPath VARCHAR(800),
date DATE NOT NULL,
studentId INT NOT NULL,
FOREIGN KEY (studentId) REFERENCES Student(studentID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Evaluation(
    evaluationID INT PRIMARY KEY AUTO_INCREMENT,
    evaluationPath VARCHAR(800),
    academicId INT,
    averageGrade DOUBLE NOT NULL,
    presentationId INT,
    FOREIGN KEY (academicId) REFERENCES Academic(academicID) ON UPDATE CASCADE,
    FOREIGN KEY (presentationId) REFERENCES Presentation(presentationID) ON UPDATE CASCADE
);

-- PROCEDURES

DELIMITER //

CREATE PROCEDURE getUserFromLogin(
IN p_userName VARCHAR(100),
IN p_password VARCHAR(100),
OUT p_userID INT,
OUT p_userRole VARCHAR(20)
)
BEGIN
DECLARE v_studentID INT DEFAULT NULL;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET p_userID = NULL;

SELECT academicID, role
INTO p_userID, p_userRole
FROM Academic
WHERE userName = p_userName AND password = p_password;

IF p_userID IS NULL THEN

SELECT studentID
INTO v_studentID
FROM Student
WHERE userName = p_userName AND password = p_password;

IF v_studentID IS NOT NULL THEN
SET p_userID = v_studentID;
SET p_userRole = 'ESTUDIANTE';
ELSE
SET p_userRole = NULL;
END IF;
END IF;
END;
//
DELIMITER ;


-- FUNCTIONS

DELIMITER //
CREATE FUNCTION findUserRole(
p_userName VARCHAR(100),
p_userPassword VARCHAR(100))
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
DECLARE userRole VARCHAR(100) DEFAULT NULL;
DECLARE v_studentID INT;

SELECT role
INTO userRole
FROM Academic
WHERE userName = p_userName AND password = p_userPassword;

IF userRole IS NULL THEN

SELECT studentID
INTO v_studentID
FROM Student
WHERE userName = p_userName AND password = p_userPassword;

IF v_studentID IS NOT NULL THEN
SET userRole = 'ESTUDIANTE';
END IF;
END IF;
RETURN userRole;
END;
//
DELIMITER ;


DELIMITER //

CREATE FUNCTION isUserFromLogin(p_userName VARCHAR(100))
RETURNS INT
DETERMINISTIC
BEGIN
DECLARE p_userID INT;

SELECT academicID INTO p_userID
FROM Academic
WHERE userName = p_userName
LIMIt 1;

IF p_userID IS NULL THEN
SELECT studentID INTO p_userID
FROM Student
WHERE userName = p_userName
LIMIT 1;
END IF;

RETURN p_userID;

END;
//
DELIMITER ;

-- USERS

DROP USER IF EXISTS coordinator@localhost;
CREATE USER coordinator@localhost IDENTIFIED BY "c00rd1n4t0R";
GRANT ALL ON ProfessionalPractices.* TO coordinator@localhost;

DROP USER IF EXISTS login_user@localhost;
CREATE USER login_user@localhost IDENTIFIED BY "l0g1NUs53r";
GRANT SELECT ON ProfessionalPractices.Academic TO login_user@localhost;
GRANT SELECT ON ProfessionalPractices.Student TO login_user@localhost;
GRANT EXECUTE ON FUNCTION ProfessionalPractices.findUserRole TO login_user@localhost;
GRANT EXECUTE ON FUNCTION ProfessionalPractices.isUserFromLogin TO login_user@localhost;

CREATE USER student_user@localhost IDENTIFIED BY 's7ud3nT';
GRANT SELECT, UPDATE ON ProfessionalPractices.Student TO student_user@localhost;
GRANT SELECT ON ProfessionalPractices.ReportTypeCatalog TO student_user@localhost;
GRANT SELECT, INSERT, UPDATE ON ProfessionalPractices.Report TO student_user@localhost;
GRANT INSERT, UPDATE ON ProfessionalPractices.ProjectRequest TO student_user@localhost;

-- TRIGGER

DELIMITER //
CREATE TRIGGER after_insert_projectAsignation
AFTER INSERT ON ProjectAsignation
FOR EACH ROW
BEGIN
UPDATE Project SET openSpots = openSpots - 1 WHERE projectID = new.projectID;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_insert_projectAsignationUpdateStudent
AFTER INSERT ON ProjectAsignation
FOR EACH ROW
BEGIN
UPDATE Student SET projectID = new.projectID 
WHERE studentID = new.studentID;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_insert_Student
AFTER INSERT ON Student
FOR EACH ROW
BEGIN
INSERT INTO StudentProgress (hoursValidated, remainingHours, tuition)
VALUES (0, 420, new.tuition);
END;
//
DELIMITER ;


DELIMITER $$
CREATE TRIGGER after_insert_Evaluation
AFTER INSERT ON Evaluation
FOR EACH ROW
BEGIN
DECLARE evaluationsCounter INT;
DECLARE totalSum DOUBLE;

SELECT COUNT(*) INTO evaluationsCounter 
FROM Evaluation 
WHERE presentationId = NEW.presentationId;

SELECT SUM(averageGrade) INTO totalSum
FROM Evaluation
WHERE presentationId = NEW.presentationId;

IF evaluationsCounter > 0 THEN

UPDATE Presentation 
SET presentationGrade = totalSum / evaluationsCounter
WHERE presentationID = NEW.presentationId;

END IF;
END;
$$
DELIMITER ;


DELIMITER $$
CREATE TRIGGER after_insert_into_report
AFTER INSERT ON Report
FOR EACH ROW
BEGIN
DECLARE t_tuition VARCHAR(10);
SELECT tuition INTO t_tuition FROM Student 
WHERE studentID = NEW.studentId;
UPDATE StudentProgress SET hoursValidated = hoursValidated + NEW.hours, 
remainingHours = remainingHours - NEW.hours 
WHERE tuition = t_tuition;
END;
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER after_delete_into_report
AFTER DELETE ON Report
FOR EACH ROW
BEGIN
DECLARE t_tuition VARCHAR(10);
SELECT tuition INTO t_tuition FROM Student 
WHERE studentID = OLD.studentId;
UPDATE StudentProgress SET hoursValidated = hoursValidated - OLD.hours, 
remainingHours = remainingHours + OLD.hours 
WHERE tuition = t_tuition;
END;
$$
DELIMITER ;



SELECT 
  a.academicID, a.name, a.firstSurname, a.secondSurname,
  e.averageGrade, e.evaluationPath,
  p.date -- o cualquier campo relevante de Presentation
FROM Academic a
JOIN Evaluation e ON a.academicID = e.academicId
JOIN Presentation p ON e.presentationId = p.presentationID;