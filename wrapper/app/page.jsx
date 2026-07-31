// Reaching this page means the proxy did not answer: either the rewrite failed to match
// or UPSTREAM_ORIGIN is wrong. It says so rather than showing a blank page, because a
// misconfigured proxy and a broken upstream look identical from the browser otherwise.
export default function Page() {
  return (
    <main style={{ fontFamily: 'system-ui, sans-serif', maxWidth: '40rem', margin: '4rem auto', padding: '0 1rem' }}>
      <h1>Compiler not reachable</h1>
      <p>
        This deployment is only a proxy — every request should have been forwarded to the
        compiler before this page was considered. Seeing it means the rewrite in{' '}
        <code>next.config.mjs</code> did not match, or <code>UPSTREAM_ORIGIN</code> does
        not point at a running instance.
      </p>
    </main>
  );
}
