# Utilities [![Build Status](https://travis-ci.org/Year4000/Utilities.svg)](https://travis-ci.org/Year4000/Utilities)

[IRC](https://webchat.esper.net/?channels=year4000): #year4000 on irc.esper.net

## About

Utilities exists for one reason only. This reason is to provide developers a set of tools.
This project is designed to be able to aid in the creation of any project not just Minecraft related.

## Core

At its core it contains a basic set of tools. But the core is split up into modules.
The modules are designed to break up parts that only some projects need and others don't.

- Core `A set of generic utility classes and functional interfaces`
- Locale `A system to load locales based on properties files`
- Redis `Set of utility classes to help with programing with redis`
- Scheduler `A very basic scheduler system`

## Sponge

Provides a basic set of tools for sponge. Parts of the system are designed to leverage sponge's design.
Each sub system uses a `interface` for the interaction and a `manager` for the brains.
This allows for other developers to extend the system with out recreating everything from scratch.

- Protocol `A tool to handle packets of the minecraft server`



## License

This project is licensed under the [GNU General Public License][license], version 3.

Copyright &copy; 2016 Year4000 [www.year4000.net][year4000]

[license]: https://www.gnu.org/copyleft/gpl.html
[year4000]: https://www.year4000.net/

