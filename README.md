# Visual Transit Simulator: Project Iteration 1

## The Visual Transit Simulator Software

In the project iterations, you will be working on a visual transit simulator (VTS) software. The current VTS software models bus transit around the University of Minnesota campus. The software simulates the behevior of busses and passengers on campus. Specifically, the busses go along a route, make stops, and pick up/droff off passengers. The simulation operates over a certain number of time units. In each time unit, the VTS software updates the state of the simulation by creating passengers at stops, moving busses along the routes, allowing a bus to pick up passengers at a stop, etc. The simulation is configured using a *configuration* file that specifies the simulation routes, the stops of the routes, and how likely it is that a passenger will show up at a specific stop in each time unit. Routes must be defined in pairs, that is, there should be both an outgoing and incoming route and the routes should be specified one after the other. The ending stop of the outgoing route should be at the same location as the starting stop of the incoming route and the ending stop of the incoming route should be at the same location as the starting stop of the outgoing route. However, stops between the starting and ending stops of outgoing and incoming routes can be at different locations. After a bus has passed a stop, it is possible for passengers to show up at stops that the bus has already passed. For this reason, the simulator supports the creation of multiple busses and these busses will go and pick up the new passengers. Each bus has its own understanding of its own route, but the stops have relationships with multiple buses. Buses do not make more than one trip through their routes. When a bus finish both of its routes (outbound and inbound), the bus exits the simulation.

The VTS software is divided into two main modules: the *visualization module* and the *simulator module*. The visualization module displays the state of the simulation in a browser, while the simulator module performs the simulation. The visualization module is a web client application that runs in a browser and it is written in Javascript and HTML. The visualization module code is inside the `<dir>/src/main/webapp/web_graphics` directory of this repo (where `<dir>` is the directory of the repo). The simulator module is a web server application written in Java. The simulator module code is inside the `<dir>/src/main/java/edu/umn/cs/csci3081w/project` directory. The simulator module is divided into two parts: *domain classes* and the *web server*. The domain classes model real-world entities (e.g., the concept of a bus) and the code is inside the `<dir>/src/main/java/edu/umn/cs/csci3081w/project/model` directory. The web server includes the code that orchestrates the simulation and is inside the `<dir>/src/main/java/edu/umn/cs/csci3081w/project/webserver` directory. The visualization module and the simulator module communicate with each other using [websockets](https://www.baeldung.com/java-websockets). In the project iterations, you will largely focus on the code of the simulator module.

The user of the VTS software interacts with the visualization module using the browser and can specific how long the simulation will run (i.e., how many time units) and how often new busses will be added to a route in the simulation. The users also specifies when to start the simulation. The image below depicts the graphical user interface (GUI) of the VTS software. 

![GUI of the VTS Software](https://www-users.cs.umn.edu/~mfazzini/teaching/csci3081w/vs_iteration_1.png)

### VTS Software Details

#### Simulation Configuration
The simulation is based on the `<dir>/src/main/resources/config.txt` configuration file. The following excerpt of the configration file defines a route.

```
ROUTE, East Bound

STOP, Blegen Hall, 44.972392, -93.243774, .15
STOP, Coffman, 44.973580, -93.235071, .3
STOP, Oak Street at University Avenue, 44.975392, -93.226632, .025
STOP, Transitway at 23rd Avenue SE, 44.975837, -93.222174, .05
STOP, Transitway at Commonwealth Avenue, 44.980753, -93.180669, .05
STOP, State Fairgrounds Lot S-108, 44.983375, -93.178810, .01
STOP, Buford at Gortner Avenue, 44.984540, -93.181692, .01
STOP, St. Paul Student Center, 44.984630, -93.186352, 0

ROUTE, West Bound

STOP, St. Paul Student Center, 44.984630, -93.186352, .35
STOP, Buford at Gortner Avenue, 44.984482, -93.181657, .05
STOP, State Fairgrounds Lot S-108, 44.983703, -93.178846, .01
STOP, Transitway at Commonwealth Avenue, 44.980663, -93.180808, .01
STOP, Thompson Center & 23rd Avenue SE, 44.976397, -93.221801, .025
STOP, Ridder Arena, 44.978058, -93.229176, .05
STOP, Pleasant Street at Jones-Eddy Circle, 44.978366, -93.236038, .1
STOP, Bruininks Hall, 44.974549, -93.236927, .3
STOP, Blegen Hall, 44.972638, -93.243591, 0
```
The line `ROUTE, East Bound` defines a the outgoing route. The lines after the line that defines a route are stops for the route. Each stop has a name, a longitude, a latitute, and the propability to generate a passenger at the stop. For example, for `STOP, Blegen Hall, 44.972392, -93.243774, .15`, `Blegen Hall` is the name of the route, `44.972392` is the longitude, `-93.243774` is the latitude, and `.15` (i.e., `0.15`) is the propability to generate a passenger at the stop.

#### Running the VTS Software
To run the VTS software, you have to first start the simulator module and then start the visualization module. To start the simulator module, go to `<dir>` and run `./gradlew appRun`. To start the visualization module, open a browser and paste this link `http://localhost:7777/project/web_graphics/project.html` in its search bar. To stop the simulator module, press the enter/return key in the terminal where you started the module. To stop the visualization module, close the tab of browser where you started the module. In rare occasions, you might experience some issues in starting the simulator module because a previous instance of the module is still running. To kill old instances, run `ps aux | grep gretty | awk '{print $2}' | xargs -L 1 kill` and this command will terminate previous instances. (The comman is killing the web server container running the simulator module.) The command works on VOLE3D and on the class VM.

#### Simulation Workflow
Because the VTS software is a web application, the software does not have a `main` method. When you load the visualization module in the browser, the visualization module opens a connection to the simulator module (using a websocket). The opening of the connection triggers the execution of the `MyWebServerSession.onOpen` method in the simulator module. When you click `Start` in the GUI of the visualization module, the module starts sending messages/commands to the simulator module. The messages/commands exchanged by the two modules are [JSON objects](https://www.w3schools.com/js/js_json_objects.asp). You can see the messages/commands created by the visualization module insdie `<dir>/src/main/webapp/web_graphics/sketch.js`. The simulator module processes messages received by the visualization model inside the `MyWebServerSession.onMessage` method. The simulator module sends messages to the visualization module using the `MyWebServerSession.sendJson` method. Finally, once you start the simulation you can restart it only by reloading the visualization module in the browser (i.e., reloading the web page of teh visualization module).

## Tasks and Deliverables
In this project iteration, you will need to understand, extend, and test the VTS software. The tasks of this project iteration can be grouped into three types of activities: creating the software documentation, making software changes, and testing. The following table provides a summary of the tasks you need to perform in this project iteration. For each task, the table reports the task ID, the activity associated with the task, a summary description of the task, the deliverable associated with the task, and the major deliverable that includes the task deliverable.

| ID | Activity | Task Summary Description | Task Deliverable | Deliverable |
|---------|----------|--------------------------|------------------|----------------------|
| Task 1 | Software documentation | Create a summary description for the VTS software | Text | HTML Javadoc |
| Task 2 | Software documentation | Create a UML class diagram for the domain classes | UML Class Diagram | HTML Javadoc |
| Task 3 | Software documentation | Create a UML class diagram for the web server classes | UML Class Diagram | HTML Javadoc |
| Task 4 | Software documentation | Create a Javadoc documentation for the code in the simulator module | HTML Javadoc | HTML Javadoc|
| Task 5 | Software documentation | Make sure that the code conforms to the Google Java code style guidelines | Source Code | Source Code |
| Task 6 | Software changes | Refactoring 1 - Change `PassengerLoader.loadPassenger` so that the method returns a `boolean` | Source Code | Source Code |
| Task 7 | Software changes | Refactoring 2 - Rename `Route.nextStop` to `Route.toNextStop` | Source Code | Source Code |
| Task 8 | Software changes | Feature 1 - Add a new route in the configuration file | Configuration Code | Source Code |
| Task 9 | Software changes | Feature 2 - Add pausing/resume capabilities to the simulator | Source Code | Source Code |
| Task 10 | Software changes | Feature 3 - Add the ability to skip a stop | Source Code | Source Code |
| Task 11 | Software changes | Feature 4 - Add generation of small, regular, and large busses to the simulator  | Source Code | Source Code |
| Task 12 | Testing | Create unit tests for the `Bus`, `Stop`, `Passenger`, and `Route` classes | Test Code | Test Code |

### Suggested Timeline for the Tasks

We suggest you define a tasks-oriented timeline for your team so that you can make progress throughout this project iteration. The schedule for project iterations is very tight, and it is important that the team keeps up with the project. We suggest the following timeline. However, you are free to define your own timeline. All the major deliverables are due at the end of the project iteration.

| Date | Milestone Description | Tasks |
|-----------------|-----|-----------------|
| 10/19/2020 at 8:00 am | Software documentation and testing | [Task 1], [Task 2], [Task 3], [Task 12] |
| 10/26/2020 at 8:00 am | Software changes | [Task 6],[Task 7],[Task 8],[Task 9],[Task 10],[Task 4],[Task 5] |
| 10/26/2020 at 8:00 am | Software changes, testing, and revision of the software documentation | [Task 11],[Task 12],[Task 1],[Task 2],[Task 3],[Task 4],[Task 5] |

### Tasks Detailed Description
This section details the tasks that your team **needs to perform** in this project iteration.

#### Task 1: Create a summary description of how to use the VTS software
Code documentation comes in many forms for many audiences. For this project, your audience is other programmers who need to understand the codebase. To this end, your team needs to produce a textual description providing an introduction to the codebase to other programmers who might be new to the project and need to know how to configure it, compile it, and execute it. (Feel free to reuse parts of the information provided in this document.) The team needs to include this textual description in the `<dir>/doc/overview.html` file. This file will be automatically added to the HTML Javadoc documentation of the project. You can generate the HTML Javadoc documentation, which includes this description, by running `./gradlew javadoc` (or `./gradlew clean javadoc`).

#### Task 2: Create a UML class diagram for the domain classes
In this task, you should produce a UML class diagram of the domain classes (e.g., classes in the `<dir>/src/main/java/edu/umn/cs/csci3081w/project/model` directory). The diagram should include only those aspects that are essential, otherwise your diagram will get too cluttered and overwhelm the reader. Keep in mind the following guidelines while creating the diagram:

* Make the most important classes prominent in the layout (i.e. your eye tends to focus in that general area when you first look at it).
* Lay out the classes so that the connections have as few crossovers as possible.
* Do not include setters and getters unless there is something special about them that your team needs to communicate.
* Include cardinality where appropriate.
* If necessary, provide a description inside `<dir>/doc/overview.html` to clarify the subtleties that are essential to understand the diagram.

You should place the diagram as am image file in `<dir>/doc/diagrams` and suitably update the `<dir>/doc/overview.html` file to include the image in the generated HTML Javadoc documentation. The file should be called `domain_diagram` with the suitable extension for the type of image file your team created. (The codebase provides an example for including an image in the `<dir>/doc/overview.html` file and your team should remove the example `umn.jpg` from the file.) You can generate the HTML Javadoc documentation, which includes this diagram, by running `./gradlew javadoc` (or `./gradlew clean javadoc`).

#### Task 3: Create a UML class diagram for the web server classes
In this task, you should produce a UML class diagram of the web server classes (e.g., classes in the `<dir>/src/main/java/edu/umn/cs/csci3081w/project/webserver` directory). The diagram should include only those aspects that are essential, otherwise your diagram will get too cluttered and overwhelm the reader. Keep in mind the following guidelines while creating the diagram:

* Make the most important classes prominent in the layout (i.e. your eye tends to focus in that general area when you first look at it).
* Lay out the classes so that the connections have as few crossovers as possible.
* Do not include setters and getters in your methods list unless there is something special about them that your team needs to communicate.
* Include cardinality where appropriate.
* If necessary, provide a description inside `<dir>/doc/overview.html` to clarify the subtleties that are essential to understand the diagram.

You should place the diagram as am image file in `<dir>/doc/diagrams` and suitably update the `<dir>/doc/overview.html` file to include the image in the generated HTML Javadoc documentation. The file should be called `webserver_diagram` with the suitable extension for the type of image file your team created. (The codebase provides an example for including an image in the `<dir>/doc/overview.html` file and your team should remove the example `umn.jpg` from the file.) You can generate the HTML Javadoc documentation, which includes this diagram, by running `./gradlew javadoc` (or `./gradlew clean javadoc`).

#### Task 4: Create a Javadoc documentation for the code in the simulator module
You should create Javadoc comments according to the Google Java code style guidelines we provided. In other words, add comments where the Google Java code style guidelines require. You can generate the HTML Javadoc documentation by running `./gradlew javadoc` (or `./gradlew clean javadoc`).

#### Task 5: Make sure that the code conforms to the Google Java code style guidelines
Consistency in code organization, naming conventions, file structure, and formatting makes code easier to read and integrate. In this project, the team will follow the Google Java code style guidelines. These guidelines are provided in the `<dir>/config/checkstyle/google_checks.xml` code style file. The team needs to make sure that the code produced in this project iteration (both source and test code) complies with the rules in `<dir>/config/checkstyle/google_checks.xml`. You can check if the code conforms to the code style rules by running `./gradlew check` or (`./gradlew clean check`).

#### Task 6: Refactoring 1 - Change `PassengerLoader.loadPassenger` so that the method returns a `boolean`
Change the method declaration of `PassengerLoader.loadPassenger` so it returns a `boolean` instead of an `int`. Make sure to also adapt the codebase (both source and possibly test code) to comply with the change (i.e., adapt uses of the `PassengerLoader.loadPassenger` method in the code). You should use the test cases you will create in Task 12 to ensure that this refactoring operation does not introduce errors in the codebase. Finally, this task should be performed in a branch called `Refactoring1`. Once the team is happy with the solution to this task, the team should merge the `Refactoring1` branch into the `main` branch.

#### Task 7: Refactoring - Rename `Route.nextStop` to `Route.toNextStop`
Rename the method declaration of `Route.nextStop` to `Route.toNextStop`. Make sure to also adapt the codebase (both source and possibly test code) to comply with the change (i.e., adapt uses of the `Route.nextStop` method in the code). You should use the test cases you will create in Task 12 to ensure that this refactoring operation does not introduce errors in the codebase. Finally, this task should be performed in a branch called `Refactoring2`. Once the team is happy with the solution to this task, the team should merge the `Refactoring2` branch into the `main` branch.

#### Task 8: Feature 1 - Add a new route in the configuration file
Add a new route to the `<dir>/src/main/resources/config.txt` configuration file and suitably name the route. Note that routes should be defined in pairs, that is, there should be both an outgoing and incoming route. You can create a new route that is a subset of the route that was given in the configuration file (i.e., to represent the concept of an express route), or an entirely new route. To obtain the longitude and latitude of locations, you can use [Google Maps](http://maps.google.com). Any route you create should use locations that are within the bounds of the map currently displayed by the simulation we gave you. It should be noted that when a real-world stop on a bus schedule has stopping locations on opposite sides of the street, the simulation treats them as two different stops. Finally, this task should be performed in a branch called `Feature1`. Once the team is happy with the solution to this task, the team should merge the `Feature1` branch into the `main` branch.

#### Task 9: Feature 2 - Add pausing/resume capabilities to the simulator
If a user presses the pause/resume button, the simulation should pause. When the user presses the button again, the simulation should resume from where it left off. This task might require the addition of a command to the set of commands already implemented in the web server. For example, the class `UpdateCommand` implements the command used to updated the simulation. Finally, this task should be performed in a branch called `Feature2`. Once the team is happy with the solution to this task, the team should merge the `Feature2` branch into the `main` branch.

#### Task 10: Feature 3 - Add the ability to skip a stop
The current version of the simulation "stops" at every stop along a route whether there are passengers at the stop or not. Add the functionality that enables a bus to skip a stop if there are no passengers waiting at an upcoming stop and no passengers are planning to get off at the upcoming stop. Finally, this task should be performed in a branch called `Feature3`. Once the team is happy with the solution to this task, the team should merge the `Feature3` branch into the `main` branch.

##### Hints
* Think about this feature in terms of loading/unloading passengers and the distance remaining to the next stop. 
* This change can be implemented by modifying (add or modify or delete) one line of code.

#### Task 11 - Feature 4: Add generation of small, regular, and large busses to the simulator
The team should extend the VTS software to create/deploy small, regular, and large capacity busses. The capacity for small, regular, and large busses is 30, 60, and 90, respectively. All busses go at the same speed. When the simulator creates/deploys a new bus, the VTS software should randomly decide which type of bus to create. To this end, you might find helpful using the [Random](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html) class from the standard Java library. Finally, this task should be performed in a branch called `Feature4`. Once the team is happy with the solution to this task, the team should merge the `Feature4` branch into the `main` branch.

#### Task 12 - Create unit tests for the `Bus`, `Stop`, `Passenger`, and `Route` classes
Unit tests are essential in a large-scale project because the entire code base can be tested regularly and automatically as it is being developed. In this project iteration, your team has to create unit tests for all the public methods in the `Bus`, `Stop`, `Passenger`, and `Route` classes. We are interested in seeing at least one test cases per method and the team does not need to create test cases for getter and setter methods. The team has to document what each test is supposed to do by adding a Javadoc comment to the test. A sample set of test cases is provided in the `<dir>/src/test/java/edu/umn/cs/csci3081w/project/model/StopTest.java` test class. We encourage your team to write the test cases before making any change to the codebase. These tests should all pass. After creating the test cases, you can perform the refactoring tasks and use the tests to ensure that your team did not introduce errors in the code. (Some test cases might need refactoring as well.) When you add the new features, you should also add new test cases for the features (when applicable). All the test cases you create should pass. Your team can create test cases in any branch but the final set of test cases should be in the `main` branch. You can run tests with the command `./gradlew test` or (`./gradlew clean test`).

## Submission
**All the team members should submit the commit ID of the solution to this project iteration on Gradescope**. The commit ID should be from the `main` branch of this repo.

### General Submission Reminders
* Use the branches we listed to produce your team solution.
* Make sure the files of your solution are in the repo.
* Do no add the content of the `build` directory to the repo.
* Make sure your code compiles.
* Make sure the code conforms to the Google Java code style guidelines we provided.
* Make sure the HTML Javadoc documentation can be generated using `./gradlew clean javadoc`
* Make sure all test cases pass.

## Assessment
The following list provides a breakdown of how this project iteration will be graded.

* Software documentation: 36 points
* Software changes: 36 points
* Testing: 28 points

## Resources

* [A Guide to the Java API for WebSocket](https://www.baeldung.com/java-websockets)
* [JSON objects](https://www.w3schools.com/js/js_json_objects.asp)
* [Google Maps](http://maps.google.com)
* [Random class from the standard Java library](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html)
