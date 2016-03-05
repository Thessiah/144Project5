Justin Hou
204155681
apjhou@gmail.com
================================

Q1)
For communications (4) -> (5) and (5) -> (6) I encrypt
the messages.  This is because only credit card info
needs to be transmitted securely.

Q2)
The item is assigned the correct buy price because when an item
is accessed via the servlet (from the database), an HTTP session
is generated and assigned its respective item buy price.  This means
that the user will not be able to tamper with the buy price value
from any sort of client-side manipulation, as it's taken care of
server-side.

Other Info)
For resources, I simply used the handy HttpSession tutorial provided
in the spec.  It took a bit of reading (especially the code) to 
figure out how to use the session to store and access information,
but once it became clear, I realized it was pretty much the same
as a request.
 
Also, at first I tried to make secure (2) -> (3) and (3) -> (4),
but afterwards realized that it did not need to be securely encrypted.
This means that the web-app performance should be better in that I have
effectively minimized encrypted access to only (4) -> (5) and (5) -> (6).