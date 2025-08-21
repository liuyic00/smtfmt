#!/bin/bash

commit_id=$(curl -s https://api.github.com/repos/liuyic00/smtfmt/branches/main | grep -o '"sha": *"[^"]*"' | head -n 1 | sed 's/"sha": "//;s/"$//' | cut -c1-7)

echo "#!/usr/bin/env -S scala-cli shebang
//> using repository jitpack
//> using dep com.github.liuyic00:smtfmt:$commit_id

import _root_.smtfmt.SmtFmt

SmtFmt.main(args)" > smtfmt.sc

chmod +x smtfmt.sc

echo "hello  smt" | ./smtfmt.sc
