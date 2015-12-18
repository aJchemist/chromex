# Tools for generating Chromex API files

To generate Chromex files, you have to clone chromium sources and point our scripts to them via `CHROMIUM_SRC` env variable.
Google's python scripts for generating [documentation site](https://developer.chrome.com/extensions/api_index) are located in [`chrome/common/extensions/docs/server2`](https://code.google.com/p/chromium/codesearch#chromium/src/chrome/common/extensions/docs/server2/).
The [`update_cache.py`](https://code.google.com/p/chromium/codesearch#chromium/src/chrome/common/extensions/docs/server2/update_cache.py)
is particularly interesting, because is used for incremental updating database of information
about APIs and it saves its state into a cache file. We leverage this knowledge and generate our files from their cache.

You should consult [their readme](https://code.google.com/p/chromium/codesearch#chromium/src/chrome/common/extensions/docs/server2/README)
on how to setup your system for `update_cache.py` to work properly. Especially this
paragraph is important:

    You MUST have branch heads fetched in your local repo in order for your
    local data set to be populated correctly. You can accomplish this by
    running:

       gclient sync --with_branch_heads
       git fetch origin

##### We provide following scripts:

  * **api-extractor.py** - takes a cache file and produces a JSON
  * **api-distiller** - takes the JSON produced by extractor and outputs distilled version which has some keys filtered out
  * **api-gen** - the actual work-horse script, which takes the distilled JSON and finally generates ClojureScript files

##### You can launch them using our wrapper shell scripts:

  * `./build-cache.sh` - builds cache from scratch (takes a long time)
  * `./update-cache.sh` - attempts to incrementally update the cache
  * `./build-api` - runs api-extractor, api-distiller and api-gen to generate Chromex files with standard settings. Produces new folders in `../src`.