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

when will happen if access token is expired but refresh token may not be expired:

API Call
   |
   ├── 200 OK → Continue
   |
   └── 401 Unauthorized
          |
          ├── Refresh succeeds → Retry original request
          |
          └── Refresh fails → Redirect to Login
