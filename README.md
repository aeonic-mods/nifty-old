# Nifty

Nifty is a cross-platform library mod that serves as a basis for tech mods targeting both Forge and Quilt/Fabric.

## Features

### Implemented
* Abstracted registry system
    * Make things exist without ever touching platform-specific code!
    * The `Registrar` class allows for registering arbitrary game objects to any Vanilla registry from your mod's entry point
    * On Fabric, objects are registered directly and available immediately in the order the `register` method is called
    * On Forge, registry is deferred to the correct Forge event via the new resource key `DeferredRegister` support ([#8527](https://github.com/MinecraftForge/MinecraftForge/pull/8527))

### Planned
* Some generic abstracted capabilities
    * Abstracted systems for item & fluid handling etc
        * Item + fluid transfer systems
    * Defer to Forge caps or the Fabric API lookup system
    * Abstracted power system, with easy implementation on blocks and items
        * Use [Tech Reborn's energy API](https://github.com/TechReborn/Energy) on Fabric
    * Interoperability with a simplified UI system, somewhat akin to Extra Utils' old machine interface mechanics
        * Easy I/O definitions in the blockentity that can be referenced and displayed with ease from a screen
* Tentatively, multiblocks
* Abstract processing implementations
    * Easily create recipes
    * Base block and BE classes to utilize those recipes
* Rendering hooks
    * Holograms
    * UI abstraction
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