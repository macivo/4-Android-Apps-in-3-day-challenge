# ![](https://www.bfh.ch/dam/jcr:36ac8a9a-6176-44fe-8e69-064cffb38e5b/logo_l-xs-home-und-footer_de.svg) Bern University of Applied Sciences
## Block Week 3 (BTI5203) 21/22
### Group 2: Mohammed Ali, Mac Müller
### Advisor: Prof. Dr. Michael Röthlin 


------------
#### ![](http://twemoji.maxcdn.com/36x36/1f4e3.png) Journal App 1: Metal Detector 

This app implementing a metal detector with a smartphone is supposed to enable user to find a hidden magnetic object at the Treasure Hunt final event. The magnetic sensor of the smartphone will be use. If there is magnetic metal near the sensor, then the sensor delivers stronger values. 
To use the app at the Treasure Hunt in the final event and to find out in which box out of a set the magnetic treasure is hidden. After the box was found, user have to scan a QR code, extract the solution code and send the solution code to the "logBuch app", which will then upload the solution code to a server. 

##### ![](http://twemoji.maxcdn.com/36x36/1f466.png) ![](http://twemoji.maxcdn.com/36x36/1f4f2.png) Mohammed Ali:
- [x] Implemented a function to detect the metal.  
- check if the smartphone i am using have a magnetic seonsor but it turns out that smartphones with compass have always this kind of sensors.  
- reading the values from the sensor and disply them using System.out.print().
- [x] Designed the user interface.
- simple UI created, a progressbar is used to display the distance to the magnetic field.
- a button is used to open the QR Reader, when the QR code is readed, the value will be directly sent to the logbook.



##### ![](http://twemoji.maxcdn.com/36x36/1f466.png) ![](http://twemoji.maxcdn.com/36x36/1f4f2.png) Mac Müller: 
 - [x] Implemented a function for reading a value from a **Q**uick **R**esponse. code
 - [x] Implemented a function to send a JSON data to "logBuch App".
 


##### ![](http://twemoji.maxcdn.com/36x36/1f6a7.png) Problems:

**1.** Code not compile with another computer.
- *Cause 1* -> GitLab was initialized from 'Android' project template.
- *Cause 2* -> Android Studio App from team members have different versions.
- [x] Solution: Deleted the template, made a blank GitLab project. Updated the lasted version of Android Studio App for all members. 

**2.**  Sometimes Application crashed, when user start to scan QR code.
- *Cause 1* -> The Application "Barcode Scanner" was newly installed and never has been run on a smartphone.
- *Cause 2* -> The Application "Barcode Scanner" is not installed on the smartphone.
- [x] Solution: After running with debugging mode, an ActivityNotFoundException has been found. Hence the exceptions must always be intercepted by calling an activity. 

**3.**  Progressbar shows always a high value.
- *Cause 1* -> This happend by Mohammed Ali, because his Smartphone was connected to his Notebook and there was headphones nearby
- [x] Solution: after moving the Smartphone to another place, the value of the Progressbar dropped.



##### ![](http://twemoji.maxcdn.com/36x36/1f6c3.png) Tests:
- [x] Scan QR codes from https://www.qrcode-generator.de/solutions/text-qr-code/.
- [x] Send the result of QR-code to Logbook app.
- [x] Check the ability of the app to find out a hidden magnet
- [x] Test the app in different places on different smartphones



##### ![](http://twemoji.maxcdn.com/36x36/1f4e5.png)  [Download This App](https://gitlab.ti.bfh.ch/mullk8/blockweek3_group2/-/blob/22fe036bd467af5fa35fcd2c416797ca395143b9/App1_Metal_Detector/GR2_metal_detector.apk) 

------------
------------
------------

#### ![](http://twemoji.maxcdn.com/36x36/1f4e3.png) Journal App 2: Memory 

This app must be able to take pictures and read the QR code and store the code words contained the QR codes in pairs, e.g., in a list or in a grid. These code word in pairs shall be sent to the "logBuch App" and thus be uploaded to a server.

##### ![](http://twemoji.maxcdn.com/36x36/1f466.png) ![](http://twemoji.maxcdn.com/36x36/1f4f2.png) Mohammed Ali:
- [x] Implemented a function to take a picture and read the QR code from a picture.
- [x] Implemented a function to send a JSON data to "logBuch App".


##### ![](http://twemoji.maxcdn.com/36x36/1f466.png) ![](http://twemoji.maxcdn.com/36x36/1f4f2.png) Mac Müller: 
 - [x] Designed with a Recycler- and Card-View for user interface.
 - [x] Helped for parsing the data to JSON format.
 


##### ![](http://twemoji.maxcdn.com/36x36/1f6a7.png) Problems:

**1.** Recycler-View does not work properly.
- *Cause 1* -> By taking more than 6 photos. If user scroll down to the end of the list and then scroll up back to the top of the list, the height of all items in Recycler-View are changed unexpected.
- [x] No solution. We could not find the solution in the estimate timing. The application can run without crashing. 

**2.** A pairing or sorting function was not implemented.
- *Cause 1* -> Insufficient development time.
- [x] Solution: Before taking photos, user must carefully choose a pair of similar images. Restart the application if user has taken a wrong photo.



##### ![](http://twemoji.maxcdn.com/36x36/1f6c3.png) Tests:
- [x] Taking 10 photos with QR code.
- [x] Test Recycler-View with more than 10 items.
- [x] Send a result of 5 items to Logbook app and check for correct data contents.

##### ![](http://twemoji.maxcdn.com/36x36/1f4e5.png)  [Download This App](https://gitlab.ti.bfh.ch/mullk8/blockweek3_group2/-/blob/22fe036bd467af5fa35fcd2c416797ca395143b9/App2_Memory/GR2_Memory.apk) 

------------
------------
------------

#### ![](http://twemoji.maxcdn.com/36x36/1f4e3.png) Journal App 3: Treasure Map

This application uses an OpenstreetMap from the OSMDroid library, which allows the user to see his position on the smartphone. The user can set markers on the map and send all the markers' positions to the "logBook app", which then uploads the solution code to a server. It should also provide a function to remove a marker.

##### ![](http://twemoji.maxcdn.com/36x36/1f466.png) ![](http://twemoji.maxcdn.com/36x36/1f4f2.png) Mohammed Ali:
- [x] Implemented the map using OSMDroid Library.
- [x] Implemented Shared-Preferences to persist geo-location.
- [x] Implemented a function to add a marker.

##### ![](http://twemoji.maxcdn.com/36x36/1f466.png) ![](http://twemoji.maxcdn.com/36x36/1f4f2.png) Mac Müller: 
- [x] Implemented a function of retrieve location, which included centering the position on the map.
- [x] Implemented a function to send a JSON data to "logBuch App".
- [x] Helped to improve the function to add a marker.
 


##### ![](http://twemoji.maxcdn.com/36x36/1f6a7.png) Problems:

**1.** The function to add the marker has slowly responding time.
- *Cause 1* -> Unknown
- [x] Solution: An application-toast(message) appears when user add/remove the marker. This way the user does not feel that the application is not responding.

##### ![](http://twemoji.maxcdn.com/36x36/1f6c3.png) Tests:
- [x] Create 5 markers on the map.
- [x] Deleting all markers on the map.
- [x] Send 2 markers to Logbook app and check for correct data contents.

##### ![](http://twemoji.maxcdn.com/36x36/1f4e5.png)  [Download This App](https://gitlab.ti.bfh.ch/mullk8/blockweek3_group2/-/blob/3175dc4fd3458e5e3694b52c413e3db3b112184b/App3_TreasureMap/GR2_TreasureMap.apk) 

------------
------------
------------

#### ![](http://twemoji.maxcdn.com/36x36/1f4e3.png) Journal App 4: Pixel Painter

This Application implementing a colored pixel painting. User can select a color and draw on a 13 x 13 Grid. A function to delete a pixel painting as well as a function to reset all pixel painting should be provided. After painting a user can send the coordinates with color-value to "logBuch App" 

##### ![](http://twemoji.maxcdn.com/36x36/1f466.png) ![](http://twemoji.maxcdn.com/36x36/1f4f2.png) Mohammed Ali:
- [x] Implemented Receiving the user's touch event
- [x] Implemented a function to send a JSON data to "logBuch App"

##### ![](http://twemoji.maxcdn.com/36x36/1f466.png) ![](http://twemoji.maxcdn.com/36x36/1f4f2.png) Mac Müller: 
- [x] Implemented Grid-Layout
- [x] Implemented UI after touch event
- [x] Helped to improve the function to send a JSON data to "logBuch App"
 

##### ![](http://twemoji.maxcdn.com/36x36/1f6a7.png) Problems:

**1.** The usage of the SquareLayout.java class was unclear.
- *Cause 1* -> The given descriptions are not clear how this class works.
- [x] Solution: We tried to implement the application without this class. Anyways we had lost much times for discovering the class.

**2.** Cause Hashset work not correctly.
- Cause -> Hashset contains our own implementation class. HashSet does not realize well that two objects are the same. 
- [x] Solution: `hashCode()` and `equals()` methods was implemented but still have some unexpected effects.




##### ![](http://twemoji.maxcdn.com/36x36/1f6c3.png) Tests:
- [x] repainting the given example.
- [x] Painting on different pixels with different colors and send them to the LoogBook app
- [x] Clear the grid

##### ![](http://twemoji.maxcdn.com/36x36/1f4e5.png)  [Download This App](https://gitlab.ti.bfh.ch/mullk8/blockweek3_group2/-/blob/master/App4_PixelMaler/GR2_PixelMaler.apk) 
