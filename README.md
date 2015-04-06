# CS 351 DLP_phase01
by 
Jessica Jones, Winston Riley, and David Ringo

## Building
find the following files `make.sh` and `run.sh` and...

~~~bash
./make.sh && ./run.sh
~~~


##Controls
### Navigation
* arrow keys pan around the globe
* shift + up arrow or down arrow zooms in and out respectively
* control clicking on a point on the map centers the camera at that point.
* mouse scroll wheel also zooms in and out

### Time
The game time mechanism can be controlled via buttons on the control panel 
* "next" will step the game forward one year
* "run" will cause the game to start stepping at roughly one year every thirty seconds
* "pause" will pause the game if it is currently running

### Inspecting
* clicking on a country will select that country for display and modification
* holding shift while clicking will add countries to any that are currently selected 
* holding shift and dragging with the mouse will allow for rectangular selection of countries
    * known bug: Radio button selection of overlays causes rectangluar selection to break  
    We are comfortable blaming the Swing framework for this 

### Info Panel

use the tabs of the info panel to display information about the currently selected countries 

* "demographic" shows country demographic data
* "land" shows a summary of land usage and provides buttons for modification
* "crops" shows a more detailed, single-crop oriented view, also with modification controls
* "overlays" provides an interface for showing different visual data on the map

-

##### Credits
text editor for XML editing was build using [RSyntaxTextArea](https://github.com/bobbylight). 
csv parsing is done with the help of the [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/) library. 
country border data from [Natural Earth](http://www.naturalearthdata.com). 
current and future raw climate data was found at [WorldClim](http://worldclim.org/). 
initial values for country data underlying our models were found from multiple resources: 

* population, birth rate, mortality, migration
    * [Population Reference Bureau](http://www.prb.org/DataFinder/)
* median age
    * [UN data](http://data.un.org/) and [CIA World Factbook](https://www.cia.gov/library/publications/the-world-factbook/)
* undernourishment, land dedication, production, imports, and exports
    * [FAOSTAT]( http://faostat3.fao.org/home/)
* GMO usage
    * [ISAAA Brief 49-2014](http://www.isaaa.org/resources/publications/briefs/49/executivesummary/default.asp)




