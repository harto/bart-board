# transit-dashboard

Polls the BART [API](http://api.bart.gov/docs/overview/index.aspx) for train
departure times at a given station.

## Development

From within Emacs, run `M-x cider-jack-in-cljs` (or `C-c M-J`).
 - REPL type: `figwheel-main`
 - Build: `dev`

From the command-line, run `lein fig:dev`

## Ideas/improvements

 - select multiple stations
 - find stations within _x_ minutes of user location (https://developer.mozilla.org/en-US/docs/Web/API/Geolocation_API)
 - include Muni times
   - https://511.org/developers/list/apis/
   - https://gist.github.com/grantland/7cf4097dd9cdf0dfed14 (?)
