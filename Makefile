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

resources/public/css/%.css: resources/public/css/%.less | node_modules
	node_modules/.bin/lessc $< $@

node_modules: package.json yarn.lock
	yarn install --pure-lockfile

clean:
	lein clean
	rm -rf resources/public/css/*.css
	rm -rf dist

distclean: clean
	rm -rf node_modules
