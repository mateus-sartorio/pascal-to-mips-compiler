const sourceCode = document.getElementById('sourceCode');
const compileButton = document.getElementById('compileButton');
const highlightLayer = document.getElementById('highlightLayer');
const editorGutter = document.getElementById('editorGutter');

const tokens = document.getElementById('tokens');
const tokenCount = document.getElementById('tokenCount');
const parseTree = document.getElementById('parseTree');
const astDot = document.getElementById('astDot');
const astImage = document.getElementById('astImage');
const parseTreeImage = document.getElementById('parseTreeImage');
const tables = document.getElementById('tables');
const mipsCode = document.getElementById('mipsCode');
const mipsGutter = document.getElementById('mipsGutter');
const mipsRuntimeGutter = document.getElementById('mipsRuntimeGutter');
const mipsRuntime = document.getElementById('mipsRuntime');
const mipsRuntimeCode = document.getElementById('mipsRuntimeCode');
const mipsRuntimeCount = document.getElementById('mipsRuntimeCount');
const issues = document.getElementById('issues');
const standardInput = document.getElementById('standardInput');
const programOutput = document.getElementById('programOutput');
const runStatus = document.getElementById('runStatus');

const editorDock = document.getElementById('editorDock');
const dockBar = document.getElementById('dockBar');
const dockToggle = document.getElementById('dockToggle');
const dockSource = document.getElementById('dockSource');
const dockHighlight = document.getElementById('dockHighlight');
const dockGutter = document.getElementById('dockGutter');
const dockCompile = document.getElementById('dockCompile');

const lexerGrammar = document.getElementById('lexerGrammar');
const lexerGutter = document.getElementById('lexerGutter');
const parserGrammar = document.getElementById('parserGrammar');
const parserGutter = document.getElementById('parserGutter');
const syntaxRules = document.getElementById('syntaxRules');

// Word-symbols from PascalLexer.g4. The grammar is caseInsensitive, so every
// lookup happens on the lowercased word. Anything the grammar does not define
// (mod, while, repeat, ...) is deliberately absent: the editor should not
// promise syntax the compiler will reject.
const KEYWORDS = new Set([
  'and', 'array', 'begin', 'div', 'do', 'downto', 'else', 'end', 'exit', 'for',
  'function', 'if', 'not', 'of', 'or', 'packed', 'procedure', 'program', 'then',
  'to', 'type', 'var'
]);
const TYPES = new Set(['boolean', 'char', 'integer', 'real', 'string']);
const LITERALS = new Set(['true', 'false']);
// Mirrors the entries registered in BuiltInProceduresAndFunctionsTable. The wording
// describes what CodeGenerator actually emits, not generic Pascal: trunc really is
// trunc.w.s (toward zero), and upcase only shifts a..z.
const BUILTIN_DOCS = {
  write:   { text: 'Writes a value to the output, leaving the cursor on the same line.', example: "write('total: ')" },
  writeln: { text: 'Writes a value to the output and starts a new line.', example: "writeln('done')" },
  read:    { text: 'Reads values from the standard input into the given variables.', example: 'read(n)' },
  readln:  { text: 'Reads values from the standard input, then skips to the next line.', example: 'readln(n)' },
  abs:     { text: 'Distance of a number from zero, dropping any minus sign.', example: 'abs(-3) = 3' },
  sqr:     { text: 'Multiplies the value by itself.', example: 'sqr(4) = 16' },
  sqrt:    { text: 'Square root of the value.', example: 'sqrt(9) = 3' },
  trunc:   { text: 'Drops the fractional part, rounding toward zero.', example: 'trunc(3.7) = 3' },
  round:   { text: 'Rounds to the nearest whole number.', example: 'round(3.7) = 4' },
  ord:     { text: 'ASCII code of a character.', example: "ord('A') = 65" },
  chr:     { text: 'Character that an ASCII code stands for.', example: "chr(65) = 'A'" },
  succ:    { text: 'Next value in order, one above the argument.', example: 'succ(4) = 5' },
  pred:    { text: 'Previous value in order, one below the argument.', example: 'pred(4) = 3' },
  length:  { text: 'How many characters a string holds.', example: "length('hello') = 5" },
  upcase:  { text: 'Uppercases a letter from a to z; anything else is returned unchanged.', example: "upcase('a') = 'A'" },
  itos:    { text: 'Converts an integer into its string form, ready to be printed.', example: "itos(42) = '42'" },
  rtos:    { text: 'Converts a real into its string form, ready to be printed.', example: "rtos(2.5) = '2.5'" },
  btos:    { text: "Converts a boolean into the string 'true' or 'false'.", example: "btos(true) = 'true'" }
};

// Derived so the editor's highlighting and the documented list can never disagree.
const BUILTINS = new Set(Object.keys(BUILTIN_DOCS));

// Ordered like the lexer: comments and strings win over everything, and
// multi-character symbols are tried before the single-character ones they
// start with (':=' before ':', '..' before '.', '(.' before '(').
const TOKEN_PATTERN = new RegExp([
  /(\{[\s\S]*?\}|\(\*[\s\S]*?\*\)|\/\/[^\n]*)/,        // 1 COMMENTARY
  /('(?:''|[^'\r\n])*')/,                              // 2 CHARACTER_STRING
  /(\d+\.\d+(?:e[+-]?\d+)?|\d+e[+-]?\d+|\d+)/,         // 3 UNSIGNED_REAL / UNSIGNED_INTEGER
  /([a-z_][a-z0-9_]*)/,                                // 4 IDENTIFIER or word-symbol
  /(:=|<>|<=|>=|\.\.|\(\.|\.\)|[+\-*/=<>])/,           // 5 operators
  /([;:,.()[\]])/                                      // 6 punctuation
].map((part) => part.source).join('|'), 'gi');

function escapeHtml(text) {
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
}

function classifyWord(word) {
  const lowered = word.toLowerCase();

  if (KEYWORDS.has(lowered)) return 'tok-keyword';
  if (TYPES.has(lowered)) return 'tok-type';
  if (LITERALS.has(lowered)) return 'tok-literal';
  if (BUILTINS.has(lowered)) return 'tok-builtin';

  return '';
}

function highlightPascal(code) {
  let html = '';
  let lastIndex = 0;
  let match;

  TOKEN_PATTERN.lastIndex = 0;

  while ((match = TOKEN_PATTERN.exec(code)) !== null) {
    const [text, comment, string, number, word, operator, punctuation] = match;

    html += escapeHtml(code.slice(lastIndex, match.index));
    lastIndex = match.index + text.length;

    let className = '';
    if (comment) className = 'tok-comment';
    else if (string) className = 'tok-string';
    else if (number) className = 'tok-number';
    else if (word) className = classifyWord(word);
    else if (operator) className = 'tok-operator';
    else if (punctuation) className = 'tok-punct';

    html += className
      ? `<span class="${className}">${escapeHtml(text)}</span>`
      : escapeHtml(text);
  }

  return html + escapeHtml(code.slice(lastIndex));
}

// Generated assembly is classified by position rather than by a list of mnemonics:
// the first word on a line is the instruction, whatever it happens to be. The code
// generator emits float and coprocessor forms (trunc.w.s, cvt.s.w, c.lt.s, bc1f)
// that any hand-maintained opcode list would eventually fall behind on.
const MIPS_TOKEN_PATTERN = new RegExp([
  /(#[^\n]*)/,                        // 1 comment
  /("(?:\\.|[^"\\\n])*")/,            // 2 string literal
  /(\$[a-z0-9]+)/,                    // 3 register
  /(\.[a-z][\w.]*)/,                  // 4 directive (.data, .asciiz, .globl)
  /([a-z_][\w.$]*)(?=\s*:)/,          // 5 label definition
  /(-?\b\d+\b)/,                      // 6 number
  /([a-z_][\w.$]*)/,                  // 7 mnemonic or symbol reference
  /([(),:])/                          // 8 punctuation
].map((part) => part.source).join('|'), 'gi');

function highlightMips(code) {
  let html = '';
  let lastIndex = 0;
  let expectMnemonic = true;
  let match;

  MIPS_TOKEN_PATTERN.lastIndex = 0;

  while ((match = MIPS_TOKEN_PATTERN.exec(code)) !== null) {
    const [text, comment, string, register, directive, label, number, word, punctuation] = match;
    const gap = code.slice(lastIndex, match.index);

    // A new line means the next bare word is an instruction again.
    if (gap.includes('\n')) {
      expectMnemonic = true;
    }

    html += escapeHtml(gap);
    lastIndex = match.index + text.length;

    let className = '';
    if (comment) {
      className = 'tok-comment';
    } else if (string) {
      className = 'tok-string';
    } else if (register) {
      className = 'tok-type';
      expectMnemonic = false;
    } else if (directive) {
      className = 'tok-builtin';
      expectMnemonic = false;
    } else if (label) {
      // A label may sit on the same line as the instruction it marks.
      className = 'tok-label';
    } else if (number) {
      className = 'tok-number';
      expectMnemonic = false;
    } else if (word) {
      className = expectMnemonic ? 'tok-keyword' : '';
      expectMnemonic = false;
    } else if (punctuation) {
      className = 'tok-punct';
    }

    html += className
      ? `<span class="${className}">${escapeHtml(text)}</span>`
      : escapeHtml(text);
  }

  return html + escapeHtml(code.slice(lastIndex));
}

// The reference panel shows the two ANTLR grammars. Rule and token definitions are
// told apart from references by position, the same trick the MIPS listing uses: a name
// followed by a colon is being defined, anywhere else it is being used.
const ANTLR_KEYWORDS = new Set([
  'grammar', 'lexer', 'parser', 'options', 'tokens', 'channels', 'import',
  'fragment', 'mode', 'returns', 'locals', 'throws', 'catch', 'finally'
]);

const ANTLR_TOKEN_PATTERN = new RegExp([
  /(\/\/[^\n]*|\/\*[\s\S]*?\*\/)/,        // 1 comment
  /('(?:\\.|[^'\\\r\n])*')/,              // 2 literal, which may contain \' escapes
  /(\[(?:\\.|[^\]\\])*\])/,               // 3 character set
  /(@[a-z_]\w*)/i,                        // 4 action, such as @header
  /(#[ \t]*[a-z_]\w*)/i,                  // 5 label on an alternative
  /(->)/,                                 // 6 lexer command arrow
  /([a-z_]\w*)(?=[ \t]*:)/i,              // 7 name being defined
  /([a-z_]\w*)/i,                         // 8 name being referenced
  /([:;|()*+?~=,.{}])/                    // 9 punctuation
  // Case-insensitive as a whole: joining the sources drops each part's own flag, and
  // without it a token name in screaming case never matches the identifier branches.
].map((part) => part.source).join('|'), 'gi');

function highlightAntlr(code) {
  let html = '';
  let lastIndex = 0;
  let afterArrow = false;
  let match;

  ANTLR_TOKEN_PATTERN.lastIndex = 0;

  while ((match = ANTLR_TOKEN_PATTERN.exec(code)) !== null) {
    const [text, comment, literal, characterSet, action, label, arrow, definition, reference, punctuation] = match;

    html += escapeHtml(code.slice(lastIndex, match.index));
    lastIndex = match.index + text.length;

    let className = '';
    if (comment) {
      className = 'tok-comment';
    } else if (literal) {
      className = 'tok-string';
    } else if (characterSet) {
      className = 'tok-literal';
    } else if (action || label) {
      className = 'tok-builtin';
    } else if (arrow) {
      className = 'tok-punct';
      afterArrow = true;
    } else if (definition) {
      className = 'tok-label';
    } else if (reference) {
      if (afterArrow) {
        // The word right after -> is a lexer command: skip, channel, more...
        className = 'tok-builtin';
        afterArrow = false;
      } else if (ANTLR_KEYWORDS.has(reference)) {
        className = 'tok-keyword';
      } else if (/^[A-Z][A-Z0-9_]*$/.test(reference)) {
        // Screaming case is the convention for tokens; anything else is a rule.
        className = 'tok-type';
      }
    } else if (punctuation) {
      className = 'tok-punct';
      if (text === ';') {
        afterArrow = false;
      }
    }

    html += className
      ? `<span class="${className}">${escapeHtml(text)}</span>`
      : escapeHtml(text);
  }

  return html + escapeHtml(code.slice(lastIndex));
}

function renderGrammar(element, gutter, source) {
  // Trailing blank lines are dropped so the rows a <pre> draws and the numbers beside
  // them stay in step, the same rule the assembly listing follows.
  const grammar = (source || '').replace(/\n+$/, '');

  element.innerHTML = grammar ? highlightAntlr(grammar) : '';
  renderGutter(gutter, grammar ? grammar.split('\n').length : 0, 1);
}

// reference/syntax-rules.txt is a list of chips rather than prose, and this reads its
// three line shapes:
//
//   # Heading          starts a section; one whose title begins with "not" is the
//                      section of things the compiler rejects, and is styled as such
//   Label: a, b, c     a row of chips under that label
//   > note             a closing remark, shown as plain text
//
// Splitting on commas is what makes "+ - * /" one chip and "and", "or", "not" three:
// the file groups the items, the renderer only draws them.
function parseSyntaxRules(text) {
  const sections = [];
  const notes = [];

  for (const rawLine of (text || '').split('\n')) {
    const line = rawLine.trim();

    if (!line) continue;

    if (line.startsWith('#')) {
      const title = line.slice(1).trim();
      sections.push({ title, missing: /^not\b/i.test(title), rows: [] });
      continue;
    }

    if (line.startsWith('>')) {
      notes.push(line.slice(1).trim());
      continue;
    }

    const separator = line.indexOf(':');

    // A line before any heading, or one without a label, has nowhere to go. Dropping
    // it silently would hide a typo in the file, so it becomes a note instead.
    if (separator === -1 || sections.length === 0) {
      notes.push(line);
      continue;
    }

    const items = line.slice(separator + 1).split(',')
      .map((item) => item.trim())
      .filter(Boolean);

    if (items.length > 0) {
      sections[sections.length - 1].rows.push({ label: line.slice(0, separator).trim(), items });
    }
  }

  return { sections, notes };
}

function chip(item, missing) {
  // Built-ins already carry a description in the symbol tables; reusing it here means
  // hovering a chip answers "what is upcase?" without opening another section.
  const documentation = BUILTIN_DOCS[item.toLowerCase()];
  // escapeHtml is written for text content, so the quote has to be handled here or a
  // description containing one would end the attribute early.
  const title = documentation
    ? ` title="${escapeHtml(documentation.text).replace(/"/g, '&quot;')}"`
    : '';

  return `<span class="chip${missing ? ' chip-missing' : ''}"${title}>${escapeHtml(item)}</span>`;
}

function renderSyntaxRules(element, text) {
  const { sections, notes } = parseSyntaxRules(text);

  const html = sections.map((section) => {
    const rows = section.rows.map((row) => ''
      + '<div class="syntax-row">'
      + `<span class="syntax-label">${escapeHtml(row.label)}</span>`
      + `<span class="chips">${row.items.map((item) => chip(item, section.missing)).join('')}</span>`
      + '</div>').join('');

    return `<section class="syntax-section${section.missing ? ' missing' : ''}">`
      + `<h4>${escapeHtml(section.title)}</h4>${rows}</section>`;
  }).join('');

  const trailing = notes.map((note) => `<p class="syntax-note">${escapeHtml(note)}</p>`).join('');

  element.innerHTML = html + trailing;
}

// Symbol tables are rendered as Pascal declarations rather than as a transcript of
// the tables. A signature says the same thing as four lines of "identifier: ..., type:
// ..." prose, and reusing the editor's token colours keeps the whole page consistent.
function typeSpan(type) {
  return `<span class="tok-type">${escapeHtml(type)}</span>`;
}

function parameterList(parameters) {
  if (!parameters || parameters.length === 0) {
    return '<span class="tok-punct">()</span>';
  }

  // read and readln are registered with a placeholder parameter because they accept
  // any number of arguments; printing "dummy: no_type" would be a lie.
  if (parameters.some((parameter) => parameter.type === 'no_type')) {
    return '<span class="tok-punct">(</span><span class="tok-comment">...</span><span class="tok-punct">)</span>';
  }

  const declarations = parameters
    .map((parameter) => `${escapeHtml(parameter.identifier)}<span class="tok-punct">:</span> ${typeSpan(parameter.type)}`)
    // Pascal separates parameter groups with a semicolon, as they would be written.
    .join('<span class="tok-punct">;</span> ');

  return `<span class="tok-punct">(</span>${declarations}<span class="tok-punct">)</span>`;
}

function routineSignature(routine) {
  const returns = routine.returnType
    ? `<span class="tok-punct">:</span> ${typeSpan(routine.returnType)}`
    : '';

  return `<span class="tok-keyword">${escapeHtml(routine.kind)}</span> `
    + `<span class="tok-builtin">${escapeHtml(routine.identifier)}</span>`
    + `${parameterList(routine.parameters)}${returns}`;
}

function lineBadge(line) {
  return line === null || line === undefined ? '' : `<span class="line-badge">line ${line}</span>`;
}

function variableItem(variable) {
  return `<li><code>${escapeHtml(variable.identifier)}<span class="tok-punct">:</span> `
    + `${typeSpan(variable.type)}</code>${lineBadge(variable.line)}</li>`;
}

function routineItem(routine) {
  const locals = routine.localVariables && routine.localVariables.length > 0
    ? `<p class="nested-title">locals</p><ul class="symbol-list nested">${routine.localVariables.map(variableItem).join('')}</ul>`
    : '';

  return `<li><code>${routineSignature(routine)}</code>${lineBadge(routine.line)}${locals}</li>`;
}

// Just the literal as it was written. The label it answers to in the .data section is the
// code generator's business, and this stage knows nothing about MIPS yet.
function literalItem(literal) {
  return `<li><code><span class="tok-string">'${escapeHtml(literal.value)}'</span></code></li>`;
}

// Each built-in expands to a one-line explanation, so the list stays scannable but
// the detail is one click away.
function builtInItem(routine) {
  const documentation = BUILTIN_DOCS[routine.identifier.toLowerCase()];
  const signature = `<code>${routineSignature(routine)}</code>`;

  if (!documentation) {
    return `<li>${signature}</li>`;
  }

  return `<li><details class="doc"><summary>${signature}</summary>`
    + `<p class="doc-text">${escapeHtml(documentation.text)}</p>`
    + `<p class="doc-example"><code>${escapeHtml(documentation.example)}</code></p>`
    + '</details></li>';
}

function symbolGroup(title, items, open) {
  const body = items.length === 0
    ? '<p class="symbol-empty">Empty</p>'
    : `<ul class="symbol-list">${items.join('')}</ul>`;

  return `<details class="symbol-group"${open ? ' open' : ''}>`
    + `<summary>${title}<span class="count">${items.length}</span></summary>${body}</details>`;
}

function renderSymbolTables(symbolTables) {
  if (!symbolTables) {
    tables.innerHTML = '';
    return;
  }

  tables.innerHTML = [
    symbolGroup('Global variables', symbolTables.globalVariables.map(variableItem), true),
    symbolGroup('Procedures and functions', symbolTables.routines.map(routineItem), true),
    symbolGroup('String literals', symbolTables.stringLiterals.map(literalItem), true),
    // The 18 built-ins are identical on every run, so they come last and start collapsed.
    symbolGroup('Built-ins', symbolTables.builtInRoutines.map(builtInItem), false)
  ].join('');
}

// Must match Constants.RUNTIME_SECTION_MARKER, the header CodeGenerator emits before
// the support routines. Everything from that line on is identical for every program,
// so it is split off and collapsed instead of padding out the interesting part.
const RUNTIME_MARKER = '# ===== RUNTIME SUPPORT ROUTINES';

function renderMips(code) {
  if (!code) {
    mipsCode.innerHTML = '';
    mipsGutter.textContent = '';
    mipsRuntime.hidden = true;
    return;
  }

  const markerIndex = code.indexOf(RUNTIME_MARKER);

  // Nothing to split on: show the listing whole rather than hiding half of it.
  if (markerIndex === -1) {
    const whole = code.replace(/\n+$/, '');

    mipsCode.innerHTML = highlightMips(whole);
    renderGutter(mipsGutter, whole.split('\n').length, 1);
    mipsRuntime.hidden = true;
    return;
  }

  // Each half drops the newline that terminates it, so the number of rows a <pre>
  // draws equals the number of lines counted here. Deriving the numbering from any
  // other string is how a gutter ends up off by one at the bottom.
  const program = code.slice(0, markerIndex).replace(/\n$/, '');
  const runtime = code.slice(markerIndex).replace(/\n+$/, '');

  const programLines = program === '' ? 0 : program.split('\n').length;
  const runtimeLines = runtime.split('\n').length;
  const total = programLines + runtimeLines;

  mipsCode.innerHTML = highlightMips(program);
  mipsRuntimeCode.innerHTML = highlightMips(runtime);
  // Numbering continues across the fold, so the range in the summary and the numbers
  // beneath it tell the same story.
  renderGutter(mipsGutter, programLines, 1);
  renderGutter(mipsRuntimeGutter, runtimeLines, programLines + 1);
  mipsRuntimeCount.textContent = `lines ${programLines + 1}-${total} of ${total}`;
  mipsRuntime.hidden = false;
}

// One number per line, counted from `from`. The gutter is a separate column, so the
// numbers stay out of anything the reader selects and copies.
function renderGutter(element, lineCount, from) {
  const numbers = [];

  for (let line = from; line < from + lineCount; line++) {
    numbers.push(line);
  }

  element.textContent = numbers.join('\n');
}

// The page shows the program twice: in the panel, and in the floating dock that takes
// over once the panel scrolls away. Each view is a transparent textarea over a coloured
// <pre> with its own gutter, so rendering takes the trio rather than reaching for the
// panel's elements by name.
const editors = [
  { textarea: sourceCode, highlight: highlightLayer, gutter: editorGutter },
  { textarea: dockSource, highlight: dockHighlight, gutter: dockGutter }
].map((editor) => ({ ...editor, box: editor.textarea.closest('.editor') }));

// The source the tokens on screen were produced from, and the token being hovered in
// that list. The range only means anything against that exact text, so an edit after a
// compile leaves the tokens on screen but stops them pointing into the editor.
let tokensSource = null;
let hoveredTokenRange = null;

function renderHighlight(editor) {
  const { value } = editor.textarea;
  // A trailing newline collapses in a <pre>, which would desynchronise
  // scrolling once the caret reaches the last line.
  const code = value.endsWith('\n') ? `${value} ` : value;
  const range = value === tokensSource ? hoveredTokenRange : null;

  editor.highlight.innerHTML = range
    ? highlightPascalAround(code, range)
    : highlightPascal(code);
  renderGutter(editor.gutter, value.split('\n').length, 1);
  syncEditorScroll(editor);
}

// Highlights the code in three pieces so the hovered token can be wrapped on its own.
// The cut points come from the lexer, so each piece holds whole tokens and colours the
// same as it would inside the undivided source.
function highlightPascalAround(code, range) {
  const start = Math.max(0, Math.min(range.start, code.length));
  const end = Math.max(start, Math.min(range.end, code.length));

  return highlightPascal(code.slice(0, start))
    + `<span class="token-hit">${highlightPascal(code.slice(start, end))}</span>`
    + highlightPascal(code.slice(end));
}

function syncEditorScroll(editor) {
  // Only the textarea gives up height to a horizontal scrollbar, so it can scroll that
  // much further than the layers behind it. They match its range by padding their own
  // bottom by the same amount, which is nothing at all when there is no scrollbar.
  const scrollbarHeight = editor.textarea.offsetHeight - editor.textarea.clientHeight;

  editor.box.style.setProperty('--scrollbar-height', `${scrollbarHeight}px`);

  editor.highlight.scrollTop = editor.textarea.scrollTop;
  editor.highlight.scrollLeft = editor.textarea.scrollLeft;
  // The gutter follows vertically only; it sits outside the horizontal scroll.
  editor.gutter.scrollTop = editor.textarea.scrollTop;
}

// Both views edit one program. Whichever the reader typed into is left untouched so the
// caret keeps its place; the other is caught up and redrawn.
function setSource(value, origin) {
  for (const editor of editors) {
    if (editor !== origin) {
      editor.textarea.value = value;
    }

    renderHighlight(editor);
  }
}

function renderPre(element, content) {
  element.textContent = content || '';
}

// Both graphs are drawn by Graphviz on the server. If it could not draw one (Graphviz
// missing, or the layout timed out) the image is null and the textual form is shown
// instead, so a stage is never simply blank.
//
// The SVG travels as markup and becomes a data URI here. Loading it through <img>
// rather than inlining the markup keeps it inert: an <img> never runs script.
function renderGraph(image, fallbackElement, source, fallbackText) {
  const rendered = Boolean(source);

  if (rendered) {
    image.src = source;
  } else {
    image.removeAttribute('src');
    image.classList.remove('zoomed');
  }

  image.hidden = !rendered;
  fallbackElement.hidden = rendered;
  renderPre(fallbackElement, rendered ? '' : fallbackText);
}

function pngSource(base64) {
  return base64 ? `data:image/png;base64,${base64}` : null;
}

function svgSource(markup) {
  return markup ? `data:image/svg+xml;charset=utf-8,${encodeURIComponent(markup)}` : null;
}

// The program runs on the server right after it compiles. It only reports a status
// when there is something to say: a clean run just shows its output.
function renderExecution(execution, compiled) {
  if (!execution) {
    programOutput.textContent = '';
    runStatus.textContent = compiled ? '' : 'not run';
    runStatus.className = compiled ? 'run-status' : 'run-status failed';
    return;
  }

  programOutput.textContent = execution.output || '';

  const notes = [];
  if (execution.timedOut) notes.push('stopped after 5s');
  if (execution.truncated) notes.push('output truncated at 64 KB');
  if (execution.error) notes.push(execution.error);

  runStatus.textContent = notes.join(' - ');
  runStatus.className = notes.length > 0 ? 'run-status failed' : 'run-status';
}

function renderIssues(list) {
  if (!list || list.length === 0) {
    issues.innerHTML = '';
    return;
  }

  issues.innerHTML = list.map((issue) => `
    <div class="issue">
      <strong>${issue.stage}</strong> [${issue.line}:${issue.column}] ${issue.message}
    </div>
  `).join('');
}

// Built from the token objects rather than by re-parsing a formatted string, so the
// colours never depend on the shape of the text. Token names are cyan here and in the
// grammar panel, which is where those same names are defined.
function renderTokens(list) {
  tokenCount.textContent = list ? String(list.length) : '0';

  if (!list || list.length === 0) {
    tokens.innerHTML = '<span class="tok-comment">No tokens produced.</span>';
    return;
  }

  // Padded to the widest entry so the columns line up; the padding is measured on the
  // raw text, before escaping turns one character such as < into several.
  const indexWidth = String(list[list.length - 1].index).length;
  const typeWidth = list.reduce((width, token) => Math.max(width, token.type.length), 0);

  // Each row carries the token's span in the source, so hovering it can light the same
  // characters up in the editor. stopIndex is inclusive, hence the +1.
  tokens.innerHTML = list.map((token) => {
    const index = String(token.index).padStart(indexWidth, ' ');
    const type = token.type.padEnd(typeWidth, ' ');

    return `<span class="token-row" data-start="${token.startIndex}" data-end="${token.stopIndex + 1}">`
      + `<span class="tok-punct">${index}.</span>  `
      + `<span class="tok-type">${escapeHtml(type)}</span>  `
      + `<span class="tok-string">"${escapeHtml(token.text)}"</span>`
      + `<span class="tok-punct">  @ </span>`
      + `<span class="tok-number">${token.line}</span><span class="tok-punct">:</span>`
      + `<span class="tok-number">${token.column}</span>`
      + '</span>';
  }).join('\n');
}

async function loadReference() {
  const response = await fetch('/api/reference');
  const reference = await response.json();

  renderGrammar(lexerGrammar, lexerGutter, reference.lexerGrammar);
  renderGrammar(parserGrammar, parserGutter, reference.parserGrammar);
  renderSyntaxRules(syntaxRules, reference.syntaxRules);
}

// The panel's button and the dock's run the same request, so they report the same state.
const compileButtons = [compileButton, dockCompile];

function setCompileButtons(label, disabled) {
  for (const button of compileButtons) {
    button.textContent = label;
    button.disabled = disabled;
  }
}

async function compileSource() {
  setCompileButtons('Running...', true);

  // Held for the whole request: the reader may keep typing while it runs, and the token
  // offsets that come back describe the text as it was submitted.
  const submitted = sourceCode.value;

  try {
    const response = await fetch('/api/compile', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ sourceCode: submitted, standardInput: standardInput.value })
    });

    const result = await response.json();

    // A stage that throws past the semantic checker answers with Spring's error body
    // instead of a CompilerResponse. Without this the panels would just go blank.
    if (!response.ok) {
      renderIssues([{ stage: 'SERVER', line: 0, column: 0, message: `${result.error || 'Request failed'} (HTTP ${response.status})` }]);
      renderExecution(null, false);
      return;
    }

    renderIssues(result.issues);
    tokensSource = submitted;
    renderTokens(result.tokens);
    renderGraph(parseTreeImage, parseTree, svgSource(result.parseTreeSvg), result.parseTree);
    renderGraph(astImage, astDot, pngSource(result.astPng), result.astDot);
    renderSymbolTables(result.symbolTables);
    renderMips(result.mipsCode);
    renderExecution(result.execution, result.success);
  } finally {
    setCompileButtons('Compile and Run', false);
  }
}

// The dock stands in for the source panel, so it appears exactly when the panel's editor
// is off-screen. Minimizing collapses it to its title bar; the choice is remembered, so
// scrolling back and forth does not undo it.
let dockMinimized = false;

function renderDockState() {
  const label = dockMinimized ? 'Maximize the floating editor' : 'Minimize the floating editor';

  editorDock.classList.toggle('is-minimized', dockMinimized);
  dockToggle.setAttribute('aria-expanded', String(!dockMinimized));
  dockToggle.setAttribute('aria-label', label);
  dockToggle.title = label;
}

function showDock(visible) {
  // A hidden card cannot hold focus, so a caret inside it is handed back to the panel
  // instead of being dropped on the body mid-edit.
  if (!visible && document.activeElement === dockSource) {
    const { selectionStart, selectionEnd } = dockSource;

    sourceCode.focus({ preventScroll: true });
    sourceCode.setSelectionRange(selectionStart, selectionEnd);
  }

  editorDock.classList.toggle('is-visible', visible);
  editorDock.setAttribute('aria-hidden', String(!visible));
}

for (const button of compileButtons) {
  button.addEventListener('click', compileSource);
}

// The button inside the bar has no handler of its own: its click bubbles to here, which
// keeps one toggle for pointer and keyboard alike.
dockBar.addEventListener('click', () => {
  dockMinimized = !dockMinimized;
  renderDockState();
});

// Hovering a row in the token list lights the token up in the editors. The listener sits
// on the list rather than on every row, so re-rendering the tokens needs no rewiring.
tokens.addEventListener('mouseover', (event) => {
  const row = event.target.closest('.token-row');

  if (row) {
    showHoveredToken({ start: Number(row.dataset.start), end: Number(row.dataset.end) });
  }
});

tokens.addEventListener('mouseleave', () => showHoveredToken(null));

function showHoveredToken(range) {
  const same = range && hoveredTokenRange
    && range.start === hoveredTokenRange.start
    && range.end === hoveredTokenRange.end;

  // mouseover fires for every element under the pointer, most of them inside the row
  // already showing.
  if (same || (!range && !hoveredTokenRange)) {
    return;
  }

  hoveredTokenRange = range;

  for (const editor of editors) {
    if (range && editor.textarea.value === tokensSource) {
      scrollLineIntoView(editor, range.start);
    }

    renderHighlight(editor);
  }
}

// Brings the line holding `offset` into the editor's own scroll box, one line of context
// clear of the edge. Every row is the same height here, so the line's offset is arithmetic
// rather than a measurement.
function scrollLineIntoView(editor, offset) {
  const { textarea } = editor;
  const style = getComputedStyle(textarea);
  const lineHeight = parseFloat(style.lineHeight);
  const line = textarea.value.slice(0, offset).split('\n').length - 1;

  const top = parseFloat(style.paddingTop) + line * lineHeight;
  const bottom = top + lineHeight;

  if (top < textarea.scrollTop) {
    textarea.scrollTop = top - lineHeight;
  }
  else if (bottom > textarea.scrollTop + textarea.clientHeight) {
    textarea.scrollTop = bottom - textarea.clientHeight + lineHeight;
  }
}

for (const image of [parseTreeImage, astImage]) {
  image.addEventListener('click', () => image.classList.toggle('zoomed'));
}

for (const editor of editors) {
  editor.textarea.addEventListener('input', () => setSource(editor.textarea.value, editor));
  editor.textarea.addEventListener('scroll', () => syncEditorScroll(editor));
}

new IntersectionObserver(
  ([entry]) => showDock(!entry.isIntersecting),
  { threshold: 0 }
).observe(sourceCode.closest('.editor'));

renderDockState();
setSource(sourceCode.value, null);
loadReference().catch((error) => {
  issues.innerHTML = `<div class="issue"><strong>REFERENCE</strong> ${error.message}</div>`;
});
compileSource().catch((error) => {
  issues.innerHTML = `<div class="issue"><strong>COMPILER</strong> ${error.message}</div>`;
});