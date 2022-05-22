# Virtual-File-System
This is a mock implementation of a virtual file system. A user is able to interact with the system through terminal commands.

# Actual storage format:
<pre>
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
-    512   x                    -   |   Number of blocks / x is allocation|       Metadata section
-    00000100011100000          -   |   Block state (full / empty)  |     < ======
-                               -
-                               -
-    root 3                     -   |   <=== Directory sub-block    |
     1 file1 ----               -   |   <=== Start of entries
     1 file2 ------
     (1 file3 23 54)
     0 newFolder 23
-    DATA...                    -   |                               |       Data section
-    DATA...                    -   |                               |           ||
-    DATA...                    -   |                               |   < ========
-            



    newFolder 0
-   |                               |
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-       
</pre>