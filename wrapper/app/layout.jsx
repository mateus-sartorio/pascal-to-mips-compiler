// The App Router refuses to build without a root layout, and this file is the whole
// reason there is an app/ directory at all. Nothing here reaches a visitor: the rewrite
// in next.config.mjs answers every path before Next looks at its own routes.
export const metadata = {
  title: 'Pascal to MIPS Compiler',
  description: 'Compiles Pascal to MIPS assembly, and runs it.',
};

export default function RootLayout({ children }) {
  return (
    <html lang="pt-BR">
      <body>{children}</body>
    </html>
  );
}
