# Andrew's Flip Card Site
This is a small web app template and data package to create simple study flip card sets.  The flip card sets are created using json files containing serialized markdown formatted text.

### Technologies Used
1. HTML 5
2. CSS
3. React
4. Java


### Dependencies
1. Java 8 JDK (any version)
2. 


### Why Write This?
Just like many of my other projects, my reason is to **learn**.  React is a very popular Java / ECMA script library, and while I used to program almost exclusively in Java 8 for nearly a *decade*, its been some time since I've used that skill.  Even more importantly, I've never used java in a web development context.


### Flip Card File Format
Each flip card is represented as an individual json file, stored in a sub-folder under the data folder.  So, your flip card stack for studying intermediate single variable calculus would be stored in a folder, perhaps called 'single-variable-calculus-practice'.  In that folder, you'd also have a single extra card called 'topic.json', which should contain meta data / description info about that flip card stack.  Card Order is determined by alphabetical sorting order of the card file names.  Perhaps the easiest way to enforce an ordering is to simply number the cards in their file names.  Ex. card1.json, card2.json, card3.json.

#### Normal Flip Card File example
```json
{
    "front": "## What is a Derivative of a Function at a Point?",
    "back": "The derivative of the function f(x) at a, denoted f`(a) is defined by f`(a) = lim[x -> a]((f(x) - f(a)) / (x - a)).  From Calculus Volume 1 By Herman and Strang, OpenStax, pg 220"
}
```


#### Flip Card Stack Meta Data File example
```json
{
    "title": "Calculus Exam 1",
    "description": "a vocab study guide for our calculus exam on November 3rd.  Includes material from chapters 1 through 4"
}
```