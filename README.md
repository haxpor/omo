# OMO
Kotlin port with changes and improvements for omo originally by ForeignGuyMike, on PC, Android and iOS (via MOE).

# What It Looks Like

## High scores feature has been added
![OMO gif 1](http://i.imgur.com/9lj8Wo4.gif)

## Smooth and Flexible drag-touch to select & deselect tiles
![OMO gif 2](http://i.imgur.com/6BKekag.gif)

# Changes

Following is changes from original project

* Support running on PC for any resolution (also support wide-screen)
* Buildable on iOS via MOE
* Fixed transition effect between game state to applies to whole screen
* Fixed touch checking for any screen resolution (via `unproject()` with specified of current `Viewport`'s width and height)
* Slightly better organization of `Tile` class spanning into
    * `SizingTile` for use as normal tile in gameplay session
    * `GlowTile` for use as tile effect when touch on tile to select it, it includes ability to set type of do contracting effect as well
    * `ExpandingTile` for use as transitioning effect when switch between one game state to another
* Flexible touch to select/deselect tiles via dragging. Support up to 2 fingers at the same time
* Save save to save highscore for each difficulty
* Added support for rendering space, + (plus), and - (minus) symbol from spritesheet
* Safer using of score to save as highscore (if beaten), and supply in Score screen
* Background music, and sfx across the game
    
# Kotlin notices

Interesting points to look at regarding Kotlin language features used in the project

* Make use of `Array` initialization with initializing function that uses `also` to reduces line of code and manual two-for-loop iteration into just technically 1 line of code.
* Use `forEach` whenever possible
* 2 dimensional array declaration i.e. `Array<Array<ExpandingTile>>`
* `open class` to allow other classes to extend it i.e `Tile` class
* Customized getter function for class's properties that not just return its backing field but involves condition checking to return `Boolean`

# libgdx notices

Interesting points to look at regarding to libgdx

* Multiple Cameras and Viewports to achieve rendering normal game state's content, and transition effect together at the same time. Lesson learned, don't forget to call `Viewport.apply()` for your current active Viewport before rendering with `SpriteBatch` to make such Viewport active.
* Use both `FitViewport` for gameplay content, and `ExtendViewport` for transition effect (whole screen)
* `Camera.unproject()` that applies screen width, and hieght from specified `Viewport` making resolution independence when check touching position

# Music

I made background music using MilkyTracker, you can give me support and download it on [Bandcamp](https://haxpor.bandcamp.com/track/8-bit-concerto).
For all sfx, I use bfxr to create them.

# Note

If you follow along with the tutorial [videos](https://www.youtube.com/watch?v=oe7_6IoFv_M&list=PL-2t7SM0vDfc7CrI_xElAP0lCIisGpsiB), you can checkout `part1` -> `part8` branch of this repository to have a ready code for end result from each video tutorial.
All in all, it's optional, and `master` is mostly the one you're looking at. 

# License

[MIT](https://github.com/haxpor/omo/blob/master/LICENSE), Wasin Thonkaew  
You should not just use code in this project, and make a copy of game to publish. Please note that ForeignGuyMike published his original idea (OMO) to [Google Play](https://play.google.com/store/apps/details?id=com.distraction.omo.android&pageId=105950263560359459987).

You can use the code, adapt it into your own project, or modify the game further with different game design etc so it's not complete rip-off, then you are clear to publish the game.

<a href="https://github.com/haxpor/donate"><img src="https://img.shields.io/badge/$-donate-ff69b4.svg?maxAge=2592000&amp;style=flat" alt="donate"></a>

For background music, it's under [CC Attibution 3.0](https://creativecommons.org/licenses/by/3.0/). That means you are free to use, modify or further use in personal or commercial project but giving credits back to me is required.
