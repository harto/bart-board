.PHONY: all css deps dist dist-css dist-html dist-js distclean prod-js

all: dist

dist: dist-html dist-js dist-css

clean:
	lein clean
	rm -rf $(CSS_DIR)
	rm -rf $(DIST)

distclean: clean
	rm -rf node_modules

## JS

CLJS_SRCS := project.clj prod.cljs.edn $(shell find src -type f -name \*.cljs)
# see prod.cljs.edn
BUILD     := target/prod
JS        := $(BUILD)/main.js
JS_MAP    := $(BUILD)/main.js.map

prod-js: $(JS) $(JS_MAP)

$(JS) $(JS_MAP): $(CLJS_SRCS)
	lein run -m figwheel.main -bo prod

## CSS

LESS_DIR := src/less
CSS_DIR := resources/public/css
LESS := $(LESS_DIR)/main.less
CSS := $(patsubst $(LESS_DIR)/%.less,$(CSS_DIR)/%.css,$(LESS))

css: $(CSS)

node_modules: package.json yarn.lock
	yarn install --pure-lockfile

$(CSS_DIR):
	mkdir -p $@

$(CSS_DIR)/%.css: $(LESS_DIR)/%.less | node_modules $(CSS_DIR)
	node_modules/.bin/lessc $< $@

## Dist

DIST := dist-build
DIST_HTML := $(DIST)/index.html
DIST_JS := $(DIST)/main.js
DIST_JS_MAP := $(DIST)/main.js.map
DIST_CSS := $(DIST)/main.css

dist-html: $(DIST_HTML)
dist-js: $(DIST_JS) $(DIST_JS_MAP)
dist-css: $(DIST_CSS)

$(DIST):
	mkdir -p $@

$(DIST_HTML): resources/public/index.html | $(DIST)
	sed -E \
		-e 's#<link.+rel="stylesheet" />#<link href="$(notdir $(DIST_CSS))" rel="stylesheet" />#' \
		-e 's#<script src="[^"]+"></script>#<script src="$(notdir $(DIST_JS))"></script>#' \
	  $< >$@

$(DIST_JS) $(DIST_JS_MAP): $(DIST)/%: $(BUILD)/% | $(DIST)
	cp $< $@

$(DIST_CSS): $(CSS) | $(DIST)
	cp $< $@
