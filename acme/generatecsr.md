```sh
D:\projects\mTLS\acme
```

`openssl req -new -nodes -out api_acme_com.csr  -keyout api_acme_com.key -config csr.conf`


* The SAN in the certificate must match the hostname used in the actual connection, not the CA.
* The SAN in the client certificate is not automatically verified by the root CA.
* It's the server application (e.g., NGINX, Spring Boot, etc.) that decides what to check — like matching the client's SAN, CN, OU, etc. — against an allowlist, policy, or ACL.
