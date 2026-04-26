JWT secutiy breakdown:

1. User logs in
2. Backend creates:
   - Access Token (15 min)
   - Refresh Token (7 days)

3. Refresh Token stored in Redis

4. Client uses Access Token for APIs

5. Access Token expires

6. Client sends Refresh Token

7. Backend validates Redis entry

8. New Access Token issued

9. User continues seamlessly

```
when will happen if access token is expired but refresh token may not be expired:

API Call
│
├── 200 OK
│   └── Continue
│
└── 401 Unauthorized
    │
    ├── Refresh Token Valid
    │   ├── Generate New Access Token
    │   └── Retry Original Request
    │
    └── Refresh Token Invalid/Expired
        └── Redirect to Login
```

```
LOGIN
  │
  ├─→ Generate Access Token  (JWT, 15 min, stateless)
  └─→ Generate Refresh Token (random UUID, 7 days)
        └─→ Save in Redis: Key="refresh:userId", Value="the-uuid-token"
        └─→ Send BOTH tokens to client

NORMAL API CALL
  Client sends Access Token → JwtAuthFilter validates it → done (Redis not touched)

ACCESS TOKEN EXPIRES
  Client sends Refresh Token to POST /auth/refresh
        └─→ Backend looks up Redis: does this token exist?
        └─→ Yes → generate new Access Token → return it
        └─→ No/Expired → 401, force re-login

LOGOUT
  Delete the refresh token from Redis
  (even if the UUID is stolen, it's now invalid)
```
