# Utilities [![Build Status](https://travis-ci.org/Year4000/Utilities.svg)](https://travis-ci.org/Year4000/Utilities) [![JitPack](https://jitpack.io/v/Year4000/Utilities.svg)](https://jitpack.io/#Year4000/Utilities)

[IRC](https://webchat.esper.net/?channels=year4000): #year4000 on irc.esper.net

The original [Utilities] created by ewized is ported over for [Year4000].
This project is a modify version used for projects made for Year4000.
Utilities was designed to be modular this allows for support on many different project levels.

## API Changes

With this project reflects the codebase on Year4000.
It our best to maintain backwards compatibility for the moment and `@Deprecated` will be removed in newer versions.
Use Utilities with caution, there is no fact that it will work the same as it does for Year4000.

## Cloning

To grab a copy of this git repo.
You will need to have [git] install on your computer;
If you are on Windows, it is recommended that you select linux like environment.

> git clone <URL> utilities

## Building

To build this project all you need to have [gradle] and [JDK].
With in gradle the project's default tasks will handle picky things.
It is recommended to use `--daemon` this will allow for faster compiling.
For even faster compiling you can select a module to build `-p core`.
This will compile every thing that it needs and nothing else.

> gradle --daemon

## Output

The output of each core module is generally inside `build/libs` though core modules shade other modules.
When there is more than one jar the suffix of `-all.jar` contains shaded jar, this is the one you want.

## Using

We use JitPack for the Maven repository hosting.

- **Repository**: [https://jitpack.io](https://jitpack.io/#Year4000/Utilities)
- **Version**: com.github.Year4000.Utilities:Utilities:master-SNAPSHOT


## Issues

Report issues at our meta discussion, [Issues](https://github.com/Year4000/Issues).

## License

Copyright &copy; 2016 Year4000 [www.year4000.net][year4000]. All Rights Reserved.

The source code is provided for reference only.
You may not redistribute modified versions of the source code.

[utilities]: https://github.com/ewized/utilities/
[year4000]: https://www.year4000.net/
[gradle]: http://gradle.org/gradle-download/
[jdk]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[git]: https://git-scm.com/download
