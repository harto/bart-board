# BART board

Polls the BART [API](http://api.bart.gov/docs/overview/index.aspx) for train
departure times at a given station.


## Development

### Prerequisites

 - Leiningen >= 2.5.3 (try `brew install leiningen` on OS X)

### Emacs / CIDER

Run `M-x cider-jack-in-cljs` (or `C-c M-J`) from any `.clj` or `.cljs` file.
 - REPL type: `figwheel-main`
 - Build: `dev`

### CLI

```console
bin/develop
```
Note: if `fswatch` is available (try `brew install fswatch`), LESS files are
automatically recompiled on save.


## Deployment

```console
make dist && bin/deploy
```
