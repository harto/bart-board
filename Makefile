.PHONY: all clean distclean

all: dist/index.html dist/main.js dist/main.js.map dist/main.css

dist:
	mkdir -p $@

dist/index.html: resources/public/index.html | dist
	sed -E \
	  -e 's#<link.+rel="stylesheet" />#<link href="main.css" rel="stylesheet" />#' \
	  -e 's#<script src="[^"]+"></script>#<script src="main.js"></script>#' \
	  $< >$@

dist/main.js dist/main.js.map: project.clj prod.cljs.edn $(shell find src -type f \( -name \*.cljs -o -name \*.clj \)) | dist
	lein trampoline run -m figwheel.main -bo prod

dist/main.css: resources/public/css/main.css | dist
	cp $< $@

resources/public/css/main.css: resources/less/main.less | node_modules resources/public/css
	node_modules/.bin/lessc $< $@

node_modules: package.json yarn.lock
	yarn install --pure-lockfile

resources/public/css:
	mkdir -p $@

clean:
	lein clean
	rm -rf $(CSS_DIR)
	rm -rf dist

distclean: clean
	rm -rf node_modules
