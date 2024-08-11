## Adding a suitable JavaFX SDK

For your convenience, if you do not have JavaFX SDK installed yet, add a compatible JavaFX SDK to this directory.

**If you do not have a JavaFX SDK and know its location on your machine:**

We suggest you download it from [here](https://gluonhq.com/products/javafx/). 
- Please select the correct JavaFX 17.0.11 (or latest stable JavaFX 17 SDK release) for your 
  - operating system
  - and CPU architecture.
- Note that **only** JavaFX SDK 17 will work, **newer versions like 21 are not supported.**
- Download, extract, and paste into this directory (./javafx-sdk)
- The integrated ```grep``` in ```runOnUnix.sh``` and ```runOnWindows.sh```, respectively, will find your installation within the directory and take care of the rest!

**Else**
- Add your custom JavaFX SDK ```/lib``` path to ```runOnUnix.sh``` and ```runOnWindows.sh``` when prompted
- That's it!
