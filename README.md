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
[Rankine Storyboard in InVision](https://projects.invisionapp.com/prototype/Rankie-Story-Board-ckkhrxoz2004hp40107txujip/play/f9d7b919)  
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
**When** the user selects options such that "Rankine" is over "Kelvin", which is over "Celsius", which is over "Fahrenheit"  
**Then** the final ranking should be in the order: "Rankine", "Kelvin", "Celsius", "Fahrenheit"  
1.3  
**Given** the user is ranking a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user selects "Rankine" over "Kelvin", selects "Kelvin" over "Fahrenheit", selects "Celsius" over "Fahrenheit", and selects "Kelvin" over "Celsius", saves and closes the ranking, then returns  
**Then** the final ranking should be in the order: "Rankine", "Kelvin", "Celsius", "Fahrenheit"  
1.4  
**Given** the user is ranking a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user selects "Rankine" over "Kelvin", saves and closes the ranking, then returns  
**Then** the user should continue the ranking, should not have to select "Rankine" over "Kelvin" again, and the final ranking should have "Rankine" placed over "Kelvin"  
### Requirement 2: Creating and Editing Elements in Data Sets  
#### Scenario  
As a user interested in ranking objects, I want to be able to create and edit my own sets of objects I want to rank so that I can rank whatever objects I want, even if no one has created a set beforehand.  
#### Dependencies  
There is an interface through which sets and objects can be created  
#### Assumptions  
The user knows how to create a set of data  
#### Examples  
2.1  
**Given** the user has an empty data set  
**When** the user adds the elements "Rankine" and "Kelvin"  
**Then** the data set should contain both "Rankine" and "Kelvin" and should be able to be ranked  
2.2  
**Given** the user has an empty data set  
**When** the user adds the element "Rankine"  
**Then** the data set should contain "Rankine" and should not be able to be ranked  
2.3  
**Given** the user has an unranked data set containing "Rankine" and "Kelvin"  
**When** the user adds the element "Fahrenheit"  
**Then** the data set should contain "Rankine", "Kelvin", and "Fahrenheit" and all three elements should be able to be ranked together  
2.4  
**Given** the user has a ranked data set containing "Rankine" over "Kelvin" over "Fahrenheit"  
**When** the user adds the element "Celsius"  
**Then** the data set should contain "Rankine", "Kelvin", "Fahrenheit", and "Celsius" and the user should be prompted to add "Celsius" into the ranking without potentially changing the relative positions of "Rankine", "Kelvin", or "Fahrenheit"  
### Requirement 3: Adding Fields and Attributes to Data Sets  
#### Scenario  
As a user interested in ranking objects, I want to be able to add fields to data sets and fill those fields with attributes in elements so that I can separate elements into different categories and look at a more interesting analysis of my ranked data.  
#### Dependencies  
A set of multiple objects exists.  
#### Assumptions  
The user knows how to add fields and attributes to sets and objects respectively.
#### Examples  
3.1  
**Given** the user has a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user creates a true/false field "Absolute Zero-based?"  
**Then** the user should be able to select the attributes "True" or "False" for the "Absolute Zero-based" field of each individual element  
3.2  
**Given** the user has a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user creates a text-based field "0-based on"  
**Then** the user should be able to input any string of characters for the "0-based on" field of each individual element  
3.3  
**Given** the user has a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user creates a number field "Freezing-point of water"  
**Then** the user should be able to select any decimal or whole number within reason for the "Freezing-point of water" field of each individual element  
3.4  
**Given** the user has a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user creates an image field "Symbol"  
**Then** the user should be able to select any image stored on their phone for the "Symbol" field of each individual element  
3.5  
**Given** the user has a data set containing the objects "Fahrenheit", "Celsius", "Kelvin", and "Rankine"  
**When** the user creates a multi-category text-based field "Vowels"  
**Then** the user should be able to input multiple character strings for the "Vowels" field of each individual element  
### Requirement 4: Filtering and Comparing Attributes  
#### Scenario  
As a user interested in ranking objects, I want to be able to filter my objects based on their attributes as well as compare the attributes themselves based on the mean or median ranking of the elements that have them so that I can get more information about how these attributes might affect my rankings.  
#### Dependencies  
A set of objects has been ranked.  
The ranked objects have their attributes filled out.  
#### Assumptions    
#### Examples  
4.1  
**Given** the user has a data set ranked: "Rankine", "Kelvin", "Celsius", "Fahrenheit" where "Rankine" and "Kelvin" have an "Absolute Zero-based?" attribute of "True" and "Celsius" and "Fahrenheit" have an "Absolute Zero-based?" attribute of "False"  
**When** the user filters based on an attribute of "True" for "Absolute Zero-based?"  
**Then** the user should see the ranking "Rankine", "Kelvin"  
4.2  
**Given** the user has a data set ranked: "Rankine", "Kelvin", "Celsius", "Fahrenheit" where "Rankine" and "Kelvin" have an "Absolute Zero-based?" attribute of "True" and "Celsius" and "Fahrenheit" have an "Absolute Zero-based?" attribute of "False"  
**When** the user compares the attributes of "Absolute Zero-based?" based on either the mean or median ranking  
**Then** the user should see the ranking "True", "False"  
4.3  
**Given** the user has a data set ranked: "Rankine", "Kelvin", "Celsius", "Fahrenheit" where "Rankine" has "Vowels" attributes of "a", "e", and "i", "Kelvin" has "Vowels" attributes of "e" and "i", "Celsius" has "Vowels" attributes of "e", "i", and "u", and "Fahrenheit" has "Vowels" attributes of "a", "e", and "i"  
**When** the user filters based on the attribute of "a" for "Vowels"  
**Then** the user should see the ranking "Rankine", "Fahrenheit"  
4.4  
**Given** the user has a data set ranked: "Rankine", "Kelvin", "Celsius", "Fahrenheit" where "Rankine" has "Vowels" attributes of "a", "e", and "i", "Kelvin" has "Vowels" attributes of "e" and "i", "Celsius" has "Vowels" attributes of "e", "i", and "u", and "Fahrenheit" has "Vowels" attributes of "a", "e", and "i"  
**When** the user compares the attributes of "Vowels" based on either the mean or median ranking  
**Then** the user should see the ranking "a" tied with "e" tied with "i", "u"  
4.5  
**Given** the user has a data set ranked: "Rankine", "Kelvin", "Celsius", "Fahrenheit" where each element has "Letters" attributes of each letter that appears in the word (ignoring dupllicates)  
**When** the user compares the attributes of "Letters" based on the mean ranking  
**Then** "n" should rank lower than "v" in the resulting letter ranking  
4.6  
**Given** the user has a data set ranked: "Rankine", "Kelvin", "Celsius", "Fahrenheit" where each element has "Letters" attributes of each letter that appears in the word (ignoring dupllicates)  
**When** the user compares the attributes of "Letters" based on the median ranking  
**Then** "n" should tie with "v" in the resulting letter ranking  
## Class Diagram  
![Rankine class diagram](https://user-images.githubusercontent.com/47836607/106396906-cb4f7500-63d8-11eb-9816-cce4236abd52.png)
### Class Diagram Description  
**MainActivity:** The first screen the user sees. Shows a list of saved ranked and unranked data sets  
**SetCreationActivity:** The screen to create/modify a set  
**SetRankActivity:** The screen to rank a set  
**ObjectSet:** A set of similar objects that can be ranked  
**RankedSet:** A set that's ranked or is in the process of being ranked  
**Element:** An object in the ObjectSet that can be ranked  
**Field:** An object that constrain element attributes  
**Data:** Objects that represent field-attribute pairs  
**IObjectSet:** An interface for ObjectSets to communicate with Firebase databases  
**IRankedSet:** An interface for RankedSets to communicate with Firebase databases  
## Scrum Roles  
- DevOps/Product Owner/Scrum Master: Colin McQueen  
- Frontend Developer: Eric Davin  
- Integration Developer: Charlie Phelps  
## Weekly Meeting  
Tuesdays and Thursdays at 4:00 PM. Meet on Microsoft Teams.  
