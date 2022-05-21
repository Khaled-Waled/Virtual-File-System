# Virtual-File-System
This is a mock implementation of a virtual file system. A user is able to interact with the system through terminal commands.

# Actual storage format:
<pre>
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
-    512                        -   |   Number of blocks            |       Metadata section
-    00000100011100000          -   |   Block state (full / empty)  |     < ======
-                               -
-                               -
-    root directory data        -   |   <=== Directory sub-block    |
-    DATA...                    -   |                               |       Data section
-    DATA...                    -   |                               |           ||
-    DATA...                    -   |                               |   < ========
-                               -   |                               |
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-       
</pre>