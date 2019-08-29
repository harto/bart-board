.PHONY: all clean

SRC_HTML := resources/public/index.html

JS_SRCS := project.clj prod.cljs.edn src/**
JS_BUILD := target/public/prod
JS_ENTRYPOINT := transit-dashboard.js

DIST := dist
DIST_HTML := $(DIST)/index.html
DIST_JS_ENTRYPOINT := $(DIST)/$(JS_ENTRYPOINT)

all: $(DIST) $(DIST_HTML) $(DIST_JS_ENTRYPOINT)

$(DIST):
	mkdir -p $(DIST)

$(DIST_HTML): $(SRC_HTML)
	sed -E "s#<script src=\"[^\"]+\"></script>#<script src=\"$(JS_ENTRYPOINT)\"></script>#" $< >$@

$(DIST_JS_ENTRYPOINT): $(JS_SRCS)
	lein trampoline run -m figwheel.main -bo prod
	cp $(JS_BUILD)/$(JS_ENTRYPOINT) $@
	cp $(JS_BUILD)/$(JS_ENTRYPOINT).map $@.map

clean:
	lein clean
	rm -rf $(DIST)