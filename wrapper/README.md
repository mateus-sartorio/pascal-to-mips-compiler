# HTTPS wrapper

Gives the compiler an HTTPS address without touching the compiler. Vercel terminates TLS
and forwards every request to the EC2 instance over HTTP; the browser only ever speaks
HTTPS. There is no iframe, because a browser blocks an HTTP iframe inside an HTTPS page
as active mixed content — and there is no need for one, since the compiler's page calls
its API on relative paths and so works unchanged behind a transparent proxy.

## Running it locally

```bash
npm install
cp .env.example .env.local     # point UPSTREAM_ORIGIN at your instance
npm run dev                    # http://localhost:3000
```

## Deploying

```bash
npm i -g vercel
vercel                         # first run links the project
vercel --prod
```

In the Vercel dashboard:

- **Root Directory** must be `wrapper`, since the repository root is the Java project.
- **Environment Variables**: set `UPSTREAM_ORIGIN`. It is read at build time, so changing
  it requires a redeploy rather than a restart.

## What this does not do

TLS ends at Vercel. The Vercel-to-EC2 leg is still plaintext HTTP across the public
internet, so submitted source code is not encrypted end to end. Closing that gap means
terminating TLS on the instance itself — a domain name plus Caddy or an ALB with an ACM
certificate — after which this wrapper is optional.

The instance also stays reachable at `http://<ip>:8080` directly, and Vercel's egress
addresses are not a fixed set, so port 8080 has to accept traffic from anywhere for the
proxy to work. Anyone with the IP bypasses the HTTPS front entirely.
