# SAN validation flow

* Server certificate is always sent first .

* Client always checks SAN vs hostname automatically .

* Server then asks for and validates the client certificate .

* SAN in client cert is not used for hostname validation but for identity/authorization, if the server is configured that way.

# Rationale for server not doing SAN check on client certs

* No natural hostname to match

* For server certs, the client knows: “I meant to call api.nationalbank.com → check SAN.”

* For client certs, the server doesn’t “dial” a hostname → there’s nothing obvious to compare SAN against. You can still do it if required