
--------------------------------------------------------------

#  NGINX for acme:
9443: Accept inbound mTLS requests from nationalbank

9000: Accept local requests and outbound mTLS to nationalbank on 8443

# NGINX for nationalbank:
8443: Accept inbound mTLS requests from acme

8000: Accept local requests and outbound mTLS to acme on 9443

Each NGINX instance needs its own config, its own certs, and runs on different ports.

-------------------------------------------------------------------------------------
| Source  | Port | Acts As     | Destination               | Expected                                 |
| ------- | ---- | ----------- | ------------------------- | ---------------------------------------- |
| nationalbank | 8000 | mTLS client | acme NGINX `9443`         | Works if acme trusts nationalbank client cert |
| acme    | 9000 | mTLS client | nationalbank NGINX `8443`      | Works if nationalbank trusts acme client cert |
| nationalbank | 8443 | mTLS server | Receives from acme client | Verifies acme cert against CA            |
| acme    | 9443 | mTLS server | Receives from nationalbank     | Verifies nationalbank cert against CA         |

-------------------------------------------------------------------------------------


Spring Boot App	Server	runs on Port 8087 (acme) & 8088 (nationalbank)

Nginx	TLS Frontend	runs on  Port 8443 (acme) & 9443 (nationalbank)

# Step 0: setup (or C:\Windows\System32\drivers\etc\hosts):

`127.0.0.1 api.acme.com`

`127.0.0.1 api.nationalbank.com`

By default, when your app or NGINX connects to https://api.acme.com or https://api.nationalbank.com, Windows will try to resolve those hostnames using DNS. But since these domains don’t really exist in the public internet (they’re just made up for your lab), DNS won’t find them.
So, you manually tell your machine in the hosts file. Whenever your system sees api.acme.com, it should resolve to 127.0.0.1 (your local machine).
Why it matters for TLS:

The certificates you generate for Acme and NationalBank will include SANs like api.acme.com and api.nationalbank.com.

When NGINX (or your app) makes an HTTPS request, it checks that the certificate’s hostname matches the domain it’s calling.

If you only used localhost, the hostname check would fail because the cert says api.acme.com, not localhost.

# Step 1: Simulate National Bank Root CA (Only one CA)
`D:\projects` is my root folder that contains mTLS code base

```sh
D:\projects\mTLS\rootCA\certs
```

`openssl genrsa -out nationalbank-rootCA.key 4096`
`openssl req -x509 -new -nodes -key nationalbank-rootCA.key -sha256 -days 3650 -out nationalbank-rootCA.crt -subj "/CN=NationalBank-Root-CA"`

Note: Root CA public key and private keys are created inside /certs folder.

# Step 2: Head on to D:\projects\mTLS\acme and follow generatecsr.md
Creates CSR request for ACME

# Step 3: Head on to D:\projects\mTLS\nationalbank and follow generatecsr.md
Creates CSR request for National Bank

# Step 4: Head on to rootCA folder and follow RootCA_README.md

Note: RootCA signs the CSR and generate client certificates.

# Step 5 : Launch NGINX Server [Two copies of nginx servers]
Download Nginx : https://nginx.org/download/nginx-1.29.0.zip

D:\projects\mTLS\NGINX-SERVERS\nginx-1.29.0-ACME> `start "" nginx -c D:\projects\mTLS\nginx-conf\nginx-acme.conf`

D:\projects\mTLS\NGINX-SERVERS\nginx-1.29.0-NATIONABANK>`start "" nginx -c D:\projects\mTLS\nginx-conf\nginx-nationalbank.conf`

# Step 5 : Launch Spring boot application Servers

Code is available under SpringBootProjects folder.

# Step 6 (test):

http://localhost:8087/call-external

http://localhost:8088/call-external

###################################################Troubleshoot########################################################

# Stop all nginx processes

KILL : `taskkill /f /im nginx.exe`

# How to kill one nginx server

`wmic process where "name='nginx.exe'" get ProcessId,CommandLine`

`taskkill /PID 6080 /F`


LIST : `tasklist /fi "imagename eq nginx.exe"`

If you’re testing from your ACME Nginx → National Bank

# Test TLS connection manually (From GIT BASH ) Not from command line]

`openssl s_client -connect api.nationalbank.com:8443 -servername api.nationalbank.com -cert D://projects//mTLS/rootCA//api_acme_com.crt -key D://projects//mTLS/acme//api_acme_com.key -CAfile D://projects//mTLS//rootCA//certs//nationalbank-rootCA.crt > test.txt;`


`openssl s_client -connect api.nationalbank.com:8443 -servername api.nationalbank.com -showcerts > showcerts.txt;`
