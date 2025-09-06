# Steps:

```sh
D:\projects\mTLS\rootCA
```

`mkdir -p newcerts`

`touch index.txt` [create a file]

`echo 1000 > serial`

# SIGN FOR ACME

`openssl ca -config openssl.cnf -in D:\projects\mTLS\acme\api_acme_com.csr -out api_acme_com.crt -batch -extfile D:\projects\mTLS\rootCA\ext\api_acme.ext -extensions v3_req`

Note : api_acme_com.crt will be created D:\projects\mTLS\rootCA. This is the client certificate used by acme during handshake

# SIGN FOR NationalBank

`openssl ca -config openssl.cnf -in D:\projects\mTLS\nationalbank\api_nationalbank_com.csr -out api_nationalbank_com.crt -batch -extfile D:\projects\mTLS\rootCA\ext\api_nationalbank.ext -extensions v3_req`

Note : api_nationalbank_com.crt will be created D:\projects\mTLS\rootCA. This is the client certificate used by national bank during handshake

# Note:
Check newcerts folder contain certificates created signed by CA.

These files are copy of the newly signed certificate that the CA keeps for its own internal records.
