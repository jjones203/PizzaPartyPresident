# CS 351 World Food Game - third phase - "Pizza World"
by 
Jessica Jones, Valarie Sheffey, Tim Chavez, Stephen Stromberg, Ken Kressin
Initial map and game framework by:  Winston Riley, and David Ringo

## Building
find the following files `make.sh` and `run.sh` and...

~~~bash
./make.sh && ./run.sh
~~~


##Controls
* Use mouse to click on continents, panels and panel controls
* On display panels, clicking on plus/minus (+ / -), or (select),
* or country names in the planning, trading and donating panels
* will select actions

### Navigation
* Use the mouse, or trackpad to scroll around the world
* If you have trackpad multi-touch capability, you can scroll using multitouch
* according to your system.
* mouse scroll wheel also zooms in and out

### Time
The game time mechanism can be controlled via the "next button on the control panel 
* "next" will step the game forward one year

### Inspecting
* clicking on a country will select that country for display and modification
* holding shift while clicking will add countries to any that are currently selected 
* holding shift and dragging with the mouse will allow for rectangular selection of countries
    * known bug: Radio button selection of overlays causes rectangluar selection to break  
    We are comfortable blaming the Swing framework for this 

### Info Panel

use the tabs of the info panel to display information about and control the currently selected countries 

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

#####initial values for country data underlying our models were found from multiple resources: 

* population, birth rate, mortality, migration 
    * [Population Reference Bureau](http://www.prb.org/DataFinder/)

* median age
    * [UN data](http://data.un.org/) and [CIA World Factbook](https://www.cia.gov/library/publications/the-world-factbook/)
* undernourishment, land dedication, production, imports, and exports
    * [FAOSTAT]( http://faostat3.fao.org/home/)
* GMO usage
    * [ISAAA Brief 49-2014](http://www.isaaa.org/resources/publications/briefs/49/executivesummary/default.asp)


##### crop preferred ranges were pulled from the following resources:

* [Fundamentals of Rice Crop Science](http://www.amazon.com/Fundamentals-Rice-Science-Shouichi-Yoshida/dp/B000NZ7ZS2) by Shouichi Yoshida
* Progress in Upland Rice Research: Proceedings of the 1985 Jakarta Conference. "An Overview of Upland Rice in the World" by Tran Van Dat.
* ["Wheat growth and physiology"](http://www.fao.org/docrep/006/y4011e/y4011e06.htm) by E. Acevedo, P. Silva, H. Silva.
* ["Crop Water Information: Wheat"](http://www.fao.org/nr/water/cropinfo_wheat.html) FAO Land & Water Division
* ["Water requirements of major crops for different agro-climatic zones of Balochistan"](http://cmsdata.iucn.org/downloads/pk_water_req.pdf) by Muhammad Ashraf and Abdul Majeed
* ["Growing Season Characteristics and Requirements in the Corn Belt"](https://www.extension.purdue.edu/extmedia/nch/nch-40.html) by Ralph E. Neild and James E. Newman.
* ["Soybean Yield Formation: What Controls It and How It Can Be Improved"](https://www.lsuagcenter.com/NR/rdonlyres/84746337-8BFE-4903-BEB8-420D0D2B7271/82639/InTechSoybean_yield_formation_what_controls_it_and.pdf)
by James E. Board and Charanjit S. Kahlon.
* ["Crop Water Information: Soybean"](http://www.fao.org/nr/water/cropinfo_soybean.html) FAO Land & Water Division 
 
