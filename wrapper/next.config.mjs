// The compiler itself runs on EC2 over plain HTTP. A browser on an HTTPS page refuses
// to load an HTTP iframe — that is active mixed content, blocked with no override — so
// this app does not embed the compiler. It proxies it: Vercel terminates TLS and then
// fetches the upstream over HTTP server-side, where mixed content does not apply. The
// browser only ever speaks HTTPS to vercel.app.
const UPSTREAM = process.env.UPSTREAM_ORIGIN ?? 'http://3.134.82.40:8080';

/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    // beforeFiles, not the plain array form: the default (afterFiles) is consulted only
    // once this app's own routes miss, which would let app/page.jsx answer "/" instead
    // of the compiler. beforeFiles runs first, so every path reaches the upstream.
    return {
      beforeFiles: [
        // The page ships its assets and calls its API on relative paths, so proxying
        // the whole tree — /, /app.js, /api/compile, /actuator/health — is enough to
        // make it work unchanged. Nothing here is specific to any one route.
        { source: '/', destination: UPSTREAM },
        { source: '/:path*', destination: `${UPSTREAM}/:path*` },
      ],
    };
  },
};

export default nextConfig;
