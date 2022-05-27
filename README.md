# Nifty

Nifty is a cross-platform library mod that serves as a basis for tech mods targeting both Forge and Quilt/Fabric.

## Features

### Implemented

* Abstracted registry system
    * Make things exist without ever touching platform-specific code!
    * The `Registrar` class allows for registering arbitrary game objects to any Vanilla registry from your mod's entry
      point
    * On Fabric, objects are registered directly and available immediately in the order the `register` method is called
    * On Forge, registry is deferred to the correct Forge event via the new resource key `DeferredRegister`
      support ([#8527](https://github.com/MinecraftForge/MinecraftForge/pull/8527))
      * May be subject to change as this only works for 1.18.2+
* Aspects
  * Original system closely mirrored Forge capabilities
  * New system is more close to the Fabric API lookup system
    * Aspect classes can be registered, then callbacks to certain objects that might contain them can be registered
    * On Fabric, this directly translates to the API lookup system
    * On Forge, lookups are tied into `getCapability` and aspects are registered as capabilities
  * Mods backed by Nifty can access Aspects registered by other mods through Nifty conditionally
  * Mods backed by Nifty can access implementations provided by other mods to a specific platform's system
    * On Forge, Aspect lookups can be used to get any registered capability
    * Registering an aspect interface on Fabric that already exists in the API lookup will just delegate lookups to the existing system
  * Mods that are _not_ backed by Nifty can access any Aspect registered through Nifty via a specific platform's system
    * A mod running on Forge can access a specific Aspect without depending on Nifty, just by using `getCapability`
    * A Fabric mod can access any Aspect without depending on Nifty by using the API lookup system
  * Some internal Aspects
    * Item handlers, with complete compatibility for existing Forge IItemHandlers and the Fabric transfer api
      * Includes a simplified implementation using Slot objects to describe an inventory's functionality
    * Fluid handlers, same as above
    * Energy handlers, same as above + compat for Reborn Team's Energy API on Fabric

### Planned

* Aspect interoperability with a simplified UI system, somewhat akin to Extra Utils' old machine interface mechanics
    * Easy I/O definitions in the blockentity that can be referenced and displayed with ease from a screen
    * Energy usage over time graph like Satisfactory's would be very cool
* Tentatively, multiblocks
* Abstract processing implementations
    * Easily create recipes
    * Base block and BE classes to utilize those recipes
    * Tie into Aspect-based blockentities and menu/screens
* Rendering hooks
    * Holograms
    * UI abstraction
      * Modular screens for block entities (so, menus too) + standalone screens
      * Selection systems with item settings and syncing
          * Radial menus from abandoned multitool project
    * Math & other rendering utils
* Abstracted configs
    * Avoid the extra dependency on Forge
    * Depend on Cloth or similar on Fabric
    * Simplified implementation as compared to Forge's configs
* Wrench configuration system
    * Blocks define behaviors for wrench configuration
    * If desired, restrict those behaviors to only work on a specific wrench
        * Probably just use a predicate for more control
* Abstracted networking
* Abstracted tags deferring to platform tags
* Abstracted data loading and data-driven "things"
    * Probably just some kind of interface that allows you to define like... anything to load from data
    * Should be easy enough with codecs