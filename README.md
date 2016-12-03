# electroncljs

A Leiningen template for [Electron](http://electron.atom.io/) 
applications written using ClojureScript, Figwheel and Reagent.

This template is based on [descjop](https://github.com/karad/lein_template_descjop).

## Usage

```
$ lein new electroncljs <your-project-name>
```

Then,

```
$ cd <your-project-name>/
$ lein npm install
```

This will download all the required NPM dependencies necessary.

Once complete you can get to work on your app.

## Getting Started With Electron App Development

I **strongly** recommend first reading the [Quick Start Guide](http://electron.atom.io/docs/tutorial/quick-start/)
on the Electron website before continuing further! At least up until it
starts talking about running/packaging your app, as that part is 
different here.

## Project Layout

#### `/app`

The main Electron app directory. Everything in here is what will be
available to your app when it is running and will be included with your
app when packaged for distribution.

You should place your app resources here such as images, CSS, other JS 
scripts, etc.

`index.html` is what will be loaded first when your application starts.

`package.json` is used by Electron to specify details about your
application and most importantly what the startup script is. You
probably won't need to change this other then updating the application
name and version as needed.

#### `/src`

Common source directory that will be available to both the Electron
main process code and the renderer process code. You can also think of
this as where you'd put your `.cljc` files if this were a typical
Clojure/ClojureScript web app.

#### `/src_front`

Source root for the Electron renderer process code. This is likely
where the vast majority of your application code will go.

#### `/src_main`

Source root for the Electron main process code.

#### `/env`

Additional source roots for `/src`, `/src_front`, and `/src_main` which
are pulled in for either development or production builds.

There is also an application config map located under
`/env/[profile]/src/[your-project-name]/config.cljs`

## Building

To build both the code for the main and renderer process:

```
$ lein build
```

Or to build each separately:

```
$ lein build-main
$ lein build-front
```

JS artifacts will be output to `/app/js`.

To clean up all build artifacts:

```
$ lein clean
```

## Running

After building, simply do:

```
$ lein run
```

And Electron should start up with your app running.

## Figwheel

IMPORTANT: You should start up Figwheel **before** Electron is running!

```
$ lein figwheel
```

And then in another terminal:

```
$ lein run
```

By default `lein figwheel` will run for the Electron renderer process
code which is probably what you will want the majority of the time.

You can connect to the Figwheel REPL on port 7888. Once connected if
you run:

```
(cljs-repl)
```

You are now set up to work with your renderer process application code.

Note that you can also use Figwheel for the Electron main process, but
do keep in mind that each time you make a code change Electron will 
restart to make sure _all_ potential main process code changes are
reloaded.

```
$ lein figwheel main
```

You can change this reload behaviour if you need to by editing the 
function `on-figwheel-reload!` located under 
`/env/dev/src_main/[your-project-name]/main/init.cljs`. If you do change
this, just remember that certain code changes won't take effect until 
Electron relaunches.

## Packaging for Release

Out of the box, you can do:

```
$ lein package
```

Which will prepare an Electron release for your app under `/releases`.
Note that with the default `project.clj` configuration, this will only
build a release for the current platform/architecture.

You can add additional platform/architectures and change other
packaging properties by editing the `package` alias in `project.clj`.

For example, to build for all platforms (Win/Linux/Mac):

```
["with-profile" "prod" "shell" "./node_modules/.bin/electron-packager"
 "app" :project/name
 ~(str "--version=" electron-version)
 "--asar"
 "--out=releases"
 "--overwrite"
 "--platform=all"]
```

Building Windows releases under Mac/Linux is supported but you will
need to install Wine.

Packaging is performed via [electron-packager](https://www.npmjs.com/package/electron-packager).

## License

Distributed under the the MIT License. See LICENSE for more details.
