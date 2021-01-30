# Rankine
---
## Introduction
What is your 32nd favorite Shakespearean play? Does your friend generally prefer halogens or noble gases? What percentage of the population's favorite temperature system is Rankine?  

Rankine provides the tools for you to:  
- Create sets of books, numbers, or anything else you might want to rank, or download sets that other people have created  
- Decide your favorite (and least favorite) things through a one-on-one matchmaking system  
- Analyze your rankings based on custom categories such as genre, relative size, or designer  
- Share and compare your rankings with friends, family, and the general public  

Make your rankings and save them to the cloud or export your favorite results to PDF to send to your friends over email. Attach images to your objects for a more visually distinct experience, and schedule reminders to continue rankings that are too large to finish in one sitting.  
## Storyboard  
## Functional Requirements  
### Requirement 1: Ranking Objects and Saving Rankings  
#### Scenario  
As a user interested in ranking objects, I want to be able to rank objects through a series of one-on-one matchups potentially over a course of multiple sessions so that I can get the list of objects in order of my preference without being overwhelmed.  
#### Dependencies  
A set of two or more objects exists to be ranked.  
#### Assumptions  
The user selects one of the two objects in each one-on-one matchup.  
The user is selecting the object they want to be ranked higher in each one-on-one matchup.  
#### Examples  
1.1  
**Given** the user is ranking a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user first selects "Rankine" over "Kelvin" and completes the rest of the ranking randomly  
**Then** the final ranking should have "Rankine" placed over "Kelvin"  
1.2  
**Given** the user is ranking a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user selects "Rankine" over "Kelvin", selects "Kelvin" over "Fahrenheit", selects "Celsius" over "Fahrenheit", and selects "Kelvin" over "Celsius"  
**Then** the final ranking should be in the order: "Rankine", "Kelvin", "Celsius", "Fahrenheit"  
1.3  
**Given** the user is ranking a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user selects "Rankine" over "Kelvin", selects "Kelvin" over "Fahrenheit", selects "Celsius" over "Fahrenheit", and selects "Kelvin" over "Celsius", saves and closes the ranking, then returns  
**Then** the final ranking should be in the order: "Rankine", "Kelvin", "Celsius", "Fahrenheit"  
1.4  
**Given** the user is ranking a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user selects "Rankine" over "Kelvin", saves and closes the ranking, then returns  
**Then** the user should continue the ranking, should not have to select "Rankine" over "Kelvin" again, and the final ranking should have "Rankine" placed over "Kelvin"  
## Class Diagram  
### Class Diagram Description  
## Scrum Roles  
- DevOps/Product Owner/Scrum Master: Colin McQueen  
- Frontend Developer: Eric Davin  
- Integration Developer: Charlie Phelps  
## Weekly Meeting  
Tuesdays and Thursdays at 4:00 PM. Meet on Microsoft Teams.  
