# Support for FarPost frameworks #

## FarPost Injector ##
* Class completion in *class* attributes of *bean* tag
* Resolving and find usages of php classes considering *class* attributes of *bean* tag
* Bean property attributes completion and resolving to php setters
* Toolkit bean name-attribute completion and resolving to php getters of IToolkit implementations

## Install ##

1. Settings | Plugins | Browse repositories... | Manage repositories...
2. Add repository url *https://raw.github.com/farpost/idea-farpost/master/plugins.xml*
3. In Browser repositories dialog select added repository (combobox on the top of dialog, 'All' by default) and reload plugin list with update button
4. Install idea-farpost plugin

### Known fuckups ###
* Class rename refactoring doesn't respect to bean references
* Find usages for setters and getters doesn't search property declarations in beans.xml

