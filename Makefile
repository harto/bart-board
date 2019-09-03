.PHONY: all js css clean distclean

JS_SRCS := project.clj prod.cljs.edn $(shell find src -type f \( -name \*.cljs -o -name \*.clj \))
JS_DIR := target/public/prod
JS_ENTRYPOINT := transit-dashboard.js
JS_FILE := $(JS_DIR)/$(JS_ENTRYPOINT)
JS_SOURCEMAP := $(JS_FILE).map

LESS_DIR := resources/less
LESS_FILES := $(shell find $(LESS_DIR) -type f -name \*.less)
CSS_DIR := resources/public/css
CSS_FILES := $(patsubst $(LESS_DIR)/%.less,$(CSS_DIR)/%.css,$(LESS_FILES))
LESSC := node_modules/.bin/lessc

DIST_HTML := dist/index.html
DIST_JS := dist/$(JS_ENTRYPOINT)
DIST_JS_SOURCEMAP := $(DIST_JS).map
DIST_CSS_DIR := dist/css
DIST_CSS := $(patsubst $(CSS_DIR)/%.css,$(DIST_CSS_DIR)/%.css,$(CSS_FILES))

all: $(DIST_HTML) $(DIST_JS) $(DIST_JS_SOURCEMAP) $(DIST_CSS)

node_modules: package.json yarn.lock
	yarn install --pure-lockfile

js: $(JS_FILE) $(JS_SOURCEMAP)

$(JS_FILE) $(JS_SOURCEMAP): $(JS_SRC_FILES)
	lein trampoline run -m figwheel.main -bo prod

css: $(CSS_FILES)

$(CSS_DIR)/%.css: $(LESS_DIR)/%.less | node_modules
	$(LESSC) $< $@

dist:
	mkdir -p $@

$(DIST_HTML): resources/public/index.html | dist
	sed -E "s#<script src=\"[^\"]+\"></script>#<script src=\"$(JS_ENTRYPOINT)\"></script>#" $< >$@

# TODO: clean up the copypasta

$(DIST_JS): $(JS_FILE) | dist
	cp $< $@

$(DIST_JS_SOURCEMAP): $(JS_SOURCEMAP) | dist
	cp $< $@

# TODO: this is probably overkill; we're only gonna have one stylesheet
$(DIST_CSS_DIR):
	mkdir -p $@

$(DIST_CSS_DIR)/%.css: $(CSS_DIR)/%.css | $(DIST_CSS_DIR)
	cp $< $@

clean:
	lein clean
	rm -rf node_modules
	rm -rf $(CSS_DIR)
	rm -rf dist

distclean: clean
	rm -rf node_modules
