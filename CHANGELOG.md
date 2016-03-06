## 0.0.8

New
* Added no-op module which can be used in release builds
* ActionBar now hides on scroll

Fixed issues
* Closing the Realm instance of RealmModelsActivity in onDestroy()



## 0.0.7

* Updated support libraries to 23.2.0
* Updated Realm version to 87.5



## 0.0.6

New
* Settings and filed selection are now accessed via a Navigation Drawer
* Display amount of objects for each model in the RealmModelsActivity (thank you @degill)
* Filter String and Integer fields :mag_right: (WIP)

Fixed issues
* Using resource prefixing (realm_browser_)
* `.log_a` and `.log_b` files not longer shown



## 0.0.5

Fixed issues
* static constant fields in realm models are no longer added as columns
* Allow Browser Activities to be started from any Context (thank you @degill)



## 0.0.4

Fixed issues
* restored compatibility to realm versions 0.83+
