# CS 351 World Food Game - third phase - "Pizza Party President"
by 
Jessica Jones, Valarie Sheffey, Tim Chavez, Stephen Stromberg, Ken Kressin

Credit for initial map and game framework to Winston Riley, and David Ringo


## Building
find the following files `make.sh` and `run.sh` and...

~~~bash
./make.sh && ./run.sh
~~~


## Game Goals
Your citizens love to party! As Pizza Party President you must be able to throw a pizza party for your continent every day while keeping the land green. Keep your people happy by feeding them their preferred pizza. Keep your land healthy by reserving water and avoiding increasing the amount of land used for planting. To win, you must finish your term (ends 2050) with a high world green rating and every continent in the world well-feed (0% hunger) with their preferred pizza.



## Pizza Preferences
Every continent has a particular pizza that it prefers. Click on the continent and view the pizza chart to determine how much of each topping the continent needs. Fully-shaded slices means the need for that topping is satisfied. Partial shading means there’s a deficit. Compare the fully-shaded portion of a slice to the partial shading to determine how severe the deficit. Hover over each slice for an info box for more in depth information.



## Approval and Diplomacy Ratings
When the Pizza Party President’s continent is selected, an approval rating and diplomacy rating will display in the demographics tab. Approval rating represents your citizen’s happiness with you. It is calculated based on the continent’s hunger and green rating. Diplomacy rating represents your representation continent’s with other continents. If your continent is well-fed but world hunger is high, your diplomacy rating will drop. Ensure to do beneficial trading and engage in foreign aid to keep your continent in good standing. Approval rating and diplomacy rating determines how much planning points you are granted each year.



## Planting
Planting occurs every year. As Pizza Party President, you must determine what percentage of land is to be used to grow each topping. You must also determine the overall percentage of toppings grown using conventional, GMO, and organic methods. GMO results in greater production, but requires research to be enviro-friendly. Organic is enviro-friendly but results in less production. Conventional is in-between.

Use the land tab, to select the percentage of each topping grown. Keep in mind your continent’s pizza preference. Also keep in mind deforestation and water usage.

###Land Tab
####The Land Tab displays:
* Total Land: Total land area of the continent

* Arable Land: Land area of the continent suitable for planting

* Open Land: Portion of arable Land not being used for planting

* Water Left: Amount of water your continent’s water allowance still available for planting

When planting, avoid deforestation by keeping the open land percentage as high as possible.

When planting, the amount of toppings able to be planted is limited by your water allowance. Each topping requires a certain amount of water. If you run out of water, even if more land is available, you may not plant anymore toppings.

###Crop Tab
####For each crop, the Crop Tab displays:
* Prod: tons of this topping produced last year

* Exported: tons of this topping exported last year

* Imported: tons of this topping imported last year

* Need: tons of pepperoni the citizens require each year (subtract need from prod to determine whether year resulted in deficit or surplus, pizza chart provides this info)

* Land – percentage of arable land dedicated to growing this topping

* Growth methods – Though listed under each topping tab, these percentages are for all toppings as a whole. They do not vary depending on topping.

The other continents plant their toppings using AI methods based on their pizza preferences and how well they met previous year needs.



## OverLays
###The overlay tab provides visual references to certain data.
* Planting: Allows player to view percentage of land used for each topping visually. Green represents open land. Hover over each colored region for an info box with detailed info about its topping.

* Population: Allows player to view which regions has the highest populations. The darker the region the greater the population. World population increases each year.

* Malnutrition: Allow player to view the malnutrition levels of the continents. The darker the continent the greater the malnutrition.

* Trading: Let’s player see which continents traded, the color of the line represents the topping traded.

* Toppings – for each topping, allows player to view percentage of that continent dedicated to specified topping. The darker the region, the higher the percentage of land dedicated to the topping

* Units – provides player the ability to switch between US and metric units.



## Planning Points
Planning point are points given based on the approval and diplomacy rating of your continent each year. Use these to invest in technology for your region or for other continents.

####The areas for planning point investment are:
* GMO Research: The higher the level of research, the more GMOs that continent is allowed to grow
* Water Efficiency: Decreases the amount of water required per crop
* Yield Efficiency: Increases yield per area of land
* Trade Efficiency: Decreases toppings lost during trade, making continent a more attractive candidate for trade

Each continent starts out at a set level for each of these categories.

The Planning Point Allocation window displays each continent’s level of technology and how many point away they are from the next level. Use ‘next’ and ‘previous’ at the top of the screen to choose the continent. Planning Points do not carry on to the next year so use them all!



## Catastrophes
Every year, there’s a random chance a catastrophe may occur. Catastrophes are events that negatively affect a continent’s yield for that year. The continent and specific event are chosen randomly.



## Trading
Trade occurs via the trade window. The continent on the top is your region. Click one of the continents at the bottom of the screen to view its toppings. Each bar represents the tons of the toppings grown that year. If the bar is behind the blue triangle, that continent has a deficit in that topping.

Trading is one-to-one, that is one crop for another. To trade with a continent, go to the toppings under your continent’s name. Press (select) for the topping your continent is going to trade. Then go to the continents on the bottom of the screen. Decide which topping you want and press select. Within the “Propose a Trade” box, use the increase and decrease button to decide the amount of toppings to be traded. When you are satisfied press “Trade these Amounts”. If you wish, continue trading the other toppings. Press “Done Trading” when you are finished. If you want to start over, press “Reset All Trades”.

Note: The other continents will not trade any toppings that are in a deficit. They also will not allow a surplus crop to be traded to the point of deficit. As Pizza Party President, you may allow your continent’s crops to be traded regardless of surplus or deficit status.

IMPORTANT: Any single trade made will be reduced by a factor determined by the continent's Trade Efficiency level. This means that a continent with a Trade Efficiency of level 4 will receive 80% of the crops traded to it (or donated to it).

The other continents trade amongst themselves after your continent finishes trading.



## Foreign Aid Window
As Pizza Party President, you a responsible for assisting other regions when in need. After the other continents trade amongst themselves, you will be asked if you want to send donations. The Foreign Aid window works similar to the trade window with the exception that the transfer is one-sided.



## Game Play – A Turn
### Game play consists of a series of turns. Each turn, the player should:
1. Examine each continent’s data
2. Adjust percentages of land dedicated to each topping and chose percentages for each growth method
3. Press next (other continents will be plant their toppings)
4. Trade with other continents (other continents will trade with each other)
5. Donate to other continents
6. Allocate planning points
7. Observe the trade overlay and view each continent’s pizza chart to see the effects of the previous year’s policies
8. Start again at 1



## Future Features
### Given more time on this project, we would like to implement:
* Global green and continental green rating: Allows user to track the environmental footprint of their decisions
* More Catastrophes: Increased variability for more fun
* Topping Storage and Water Storage: Allows players to plan for droughts or food shortages
* Multiplayer: Allow user to replace AI in making continent decisions
* Climate Change Effects: Currently implemented in this model as a yearly negative effect on yield, but could be done more sophisticatedly (maybe effect incidences of catastrophes)
* Abdication: If your Approval Rating gets too low, you're kicked out of office and the game is over.
* Warfare: If your Diplomacy Rating gets too low, other regions declare war on you.
* Summary Screen: At end of game, show user-friendly statistics in graph format



## Development
### This game was developed following an iterative methodology:
* Contacted client for further specifications
* Brainstormed and created game story
* Collaborated with client to ensure story fit specifications
* Determined roles and began programming
* Constant revising of story and modification of roles as better understanding of requirements developed
* Presentation preparation
* Game debug and clean-up
* Submission



##Controls
* Use mouse to click on continents, panels and panel controls
* In display panels, clicking on plus/minus (+ / -), or (select),
* In trading, donating, or planning point allocation window, click on continent names to display (select) options


### Navigation
* Use the mouse, or trackpad to scroll around the world
* If you have trackpad multi-touch capability, you can scroll using multitouch
* according to your system.
* mouse scroll wheel also zooms in and out


### Credits
text editor for XML editing was build using [RSyntaxTextArea](https://github.com/bobbylight). 
csv parsing is done with the help of the [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/) library. 
continent border data from [Natural Earth](http://www.naturalearthdata.com). 
current and future raw climate data was found at [WorldClim](http://worldclim.org/). 

#####initial values for continent data underlying our models were found from multiple resources: 

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

##### Topping images courtesy of:

* Tomato – http://cliparts.co/cliparts/pi7/8o9/pi78o9x5T.png
* Pineapples – http://cliparts.co/cliparts/Bcg/KxM/BcgKxMbxi.jpg
* Pepper – http://cliparts.co/cliparts/8TA/6Rb/8TA6Rbkpc.png
* Pepperoni – http://www.clipartlord.com/wp-content/uploads/2013/05/salami.png
* Mushrooms – http://www.clker.com/cliparts/h/c/k/x/B/p/mushroom.svg

