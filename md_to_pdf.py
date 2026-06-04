import re
import unicodedata
import markdown
from fpdf import FPDF

MD_FILE = "BottomNavAndDrawer.md"
PDF_FILE = "BottomNavAndDrawer.pdf"

_EXPLICIT = {
    "—": " - ",   # em-dash
    "–": "-",     # en-dash
    "→": "->",    # right arrow
    "←": "<-",    # left arrow
    "└": "  ",    # box: up and right
    "├": "  ",    # box: vertical and right
    "─": "-",     # box: horizontal
    "│": "|",     # box: vertical
    "‘": "'",     # left single quote
    "’": "'",     # right single quote
    "“": '"',     # left double quote
    "”": '"',     # right double quote
    "•": "-",     # bullet
    " ": " ",     # non-breaking space
    "…": "...",   # ellipsis
    "✔": "OK",    # check mark
}

def sanitize(text):
    result = []
    for ch in text:
        if ord(ch) < 256:
            result.append(ch)
        elif ch in _EXPLICIT:
            result.append(_EXPLICIT[ch])
        else:
            nkfd = unicodedata.normalize("NFKD", ch)
            ascii_ch = nkfd.encode("ascii", "ignore").decode("ascii")
            result.append(ascii_ch if ascii_ch else "?")
    return "".join(result)


# ---------------------------------------------------------------------------
# Read and convert markdown to HTML
# ---------------------------------------------------------------------------

with open(MD_FILE, encoding="utf-8") as f:
    md_text = f.read()

html = markdown.markdown(md_text, extensions=["tables", "fenced_code"])


# ---------------------------------------------------------------------------
# PDF class
# ---------------------------------------------------------------------------

class PDF(FPDF):
    def header(self):
        pass

    def footer(self):
        self.set_y(-12)
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(150)
        self.cell(0, 10, f"Page {self.page_no()}", align="C")

    def h1(self, text):
        self.ln(4)
        self.set_font("Helvetica", "B", 20)
        self.set_text_color(30, 30, 30)
        self.multi_cell(0, 10, sanitize(text))
        self.ln(2)
        self.set_draw_color(80, 100, 180)
        self.set_line_width(0.6)
        self.line(self.l_margin, self.get_y(), self.w - self.r_margin, self.get_y())
        self.ln(4)

    def h2(self, text):
        self.ln(5)
        self.set_font("Helvetica", "B", 14)
        self.set_text_color(50, 70, 160)
        self.multi_cell(0, 8, sanitize(text))
        self.ln(2)

    def h3(self, text):
        self.ln(3)
        self.set_font("Helvetica", "B", 11)
        self.set_text_color(60, 60, 60)
        self.multi_cell(0, 7, sanitize(text))
        self.ln(1)

    def paragraph(self, text):
        self.set_font("Helvetica", "", 10)
        self.set_text_color(30, 30, 30)
        text = sanitize(re.sub(r"`([^`]+)`", r"\1", text))
        self.multi_cell(0, 5.5, text)
        self.ln(2)

    def bullet(self, text, depth=0):
        self.set_font("Helvetica", "", 10)
        self.set_text_color(30, 30, 30)
        indent = 8 + depth * 6
        bullet_x = self.l_margin + indent
        text_x = bullet_x + 5
        self.set_x(bullet_x)
        self.cell(5, 5.5, "-")
        self.set_x(text_x)
        text = sanitize(re.sub(r"`([^`]+)`", r"\1", text))
        self.multi_cell(0, 5.5, text)

    def code_block(self, text):
        self.ln(2)
        self.set_fill_color(245, 245, 245)
        self.set_draw_color(200, 200, 200)
        self.set_font("Courier", "", 7.5)
        self.set_text_color(30, 30, 30)
        lines = text.split("\n")
        line_h = 4.5
        block_h = len(lines) * line_h + 4
        self.set_line_width(0.2)
        self.rect(self.l_margin, self.get_y(), self.w - self.l_margin - self.r_margin, block_h, "FD")
        self.set_xy(self.l_margin + 3, self.get_y() + 2)
        for line in lines:
            self.set_x(self.l_margin + 3)
            self.cell(0, line_h, sanitize(line))
            self.ln(line_h)
        self.ln(3)

    def hr(self):
        self.ln(2)
        self.set_draw_color(180, 180, 180)
        self.set_line_width(0.3)
        self.line(self.l_margin, self.get_y(), self.w - self.r_margin, self.get_y())
        self.ln(4)

    def table(self, headers, rows):
        self.ln(2)
        col_w = (self.w - self.l_margin - self.r_margin) / len(headers)
        row_h = 6

        self.set_fill_color(80, 100, 180)
        self.set_text_color(255, 255, 255)
        self.set_font("Helvetica", "B", 9)
        for h in headers:
            self.cell(col_w, row_h, sanitize(h.strip()), border=1, fill=True)
        self.ln()

        self.set_text_color(30, 30, 30)
        self.set_font("Helvetica", "", 9)
        for i, row in enumerate(rows):
            fill = (i % 2 == 0)
            if fill:
                self.set_fill_color(235, 238, 250)
            else:
                self.set_fill_color(255, 255, 255)
            for cell in row:
                cell_text = sanitize(re.sub(r"`([^`]+)`", r"\1", cell.strip()))
                self.multi_cell(col_w, row_h, cell_text, border=1, fill=fill, new_x="RIGHT", new_y="TOP")
            self.ln()
        self.ln(3)


# ---------------------------------------------------------------------------
# Parse HTML into PDF
# ---------------------------------------------------------------------------

pdf = PDF()
pdf.set_margins(18, 18, 18)
pdf.set_auto_page_break(auto=True, margin=20)
pdf.add_page()

lines = html.split("\n")
i = 0
while i < len(lines):
    line = lines[i].strip()

    if not line:
        i += 1
        continue

    if line.startswith("<h1>"):
        pdf.h1(re.sub(r"<[^>]+>", "", line))

    elif line.startswith("<h2>"):
        pdf.h2(re.sub(r"<[^>]+>", "", line))

    elif line.startswith("<h3>"):
        pdf.h3(re.sub(r"<[^>]+>", "", line))

    elif line == "<hr />":
        pdf.hr()

    elif line.startswith("<pre>"):
        code_lines = []
        i += 1
        while i < len(lines) and not lines[i].strip().startswith("</pre>"):
            raw = lines[i]
            raw = re.sub(r"<code[^>]*>", "", raw)
            raw = re.sub(r"</code>", "", raw)
            raw = raw.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("&#39;", "'").replace("&quot;", '"')
            code_lines.append(raw)
            i += 1
        pdf.code_block("\n".join(code_lines))

    elif line.startswith("<table>"):
        headers = []
        rows = []
        in_head = False
        current_row = []
        i += 1
        while i < len(lines) and not lines[i].strip().startswith("</table>"):
            l = lines[i].strip()
            if l == "<thead>":
                in_head = True
            elif l == "</thead>":
                in_head = False
            elif l in ("<tbody>", "</tbody>"):
                pass
            elif l == "<tr>":
                current_row = []
            elif l == "</tr>":
                if in_head:
                    headers = current_row
                else:
                    rows.append(current_row)
            elif l.startswith("<th>") or l.startswith("<td>"):
                cell = re.sub(r"<[^>]+>", "", l)
                current_row.append(cell)
            i += 1
        if headers:
            pdf.table(headers, rows)

    elif line.startswith("<ul>"):
        i += 1
        while i < len(lines) and not lines[i].strip().startswith("</ul>"):
            l = lines[i].strip()
            if l.startswith("<li>"):
                text = re.sub(r"<[^>]+>", "", l)
                text = text.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&")
                pdf.bullet(text)
            i += 1

    elif line.startswith("<ol>"):
        counter = 1
        i += 1
        while i < len(lines) and not lines[i].strip().startswith("</ol>"):
            l = lines[i].strip()
            if l.startswith("<li>"):
                text = re.sub(r"<[^>]+>", "", l)
                text = text.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&")
                pdf.bullet(f"{counter}. {text}")
                counter += 1
            i += 1

    elif line.startswith("<p>"):
        text = re.sub(r"<[^>]+>", "", line)
        text = text.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("&#39;", "'").replace("&quot;", '"')
        if text.strip():
            pdf.paragraph(text)

    i += 1

pdf.output(PDF_FILE)
print(f"PDF written to {PDF_FILE}")
