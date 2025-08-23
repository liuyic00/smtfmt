# SMT-LIB Formatter

*Notice: the functions are fairly basic right now.*

## Usage

### Through Scala-cli

Generate the Scala script `smtfmt.sc` to the current folder with latest
`smtfmt`:

```bash
curl -L liuyic00.github.io/smtfmt/init.sh | sh
```

Call `./smtfmt.sc` to format.
`smtfmt.sc` will read in from STDIN and print the formatted SMT to STDOUT.

Running the Scala script can be a little slow.
To speed it up, package the script as `smtfmt`:

``` bash
scala-cli --power package smtfmt.sc -o smtfmt --assembly
```

### Through Source Code and Mill

Clone the project and assembly using mill.

```bash
git clone https://github.com/liuyic00/smtfmt.git
cd smtfmt
./mill smtfmt.assembly
```

The executable fill will be located at `out/smtfmt/assembly.dest/out.jar`.

## Use in VS Code

Once you have the executable script/jar/bin, use it in VS Code with the
extension
[Custom Local Formatters](https://marketplace.visualstudio.com/items?itemName=jkillian.custom-local-formatters).

Install the extension and add the following content to you `settings.json`:

```json
  "customLocalFormatters.formatters": [
    {
      // The command will be run with a working directory of the workspace root.
      "command": "./smtfmt",  // or other path to call the executable file.
      "languages": [
        "smt",
        "smt-lib"
      ]
    }
  ]
```
