# Example Binary Server

This is a very basic (read *highly flaky and deeply insecure*) example of a binary file server in Java. It's not an Eclipse project and uses default packages so can just be compiled with javac.

Once compiled you can run FileServer and FileClient.

```java FileServer [port]``` runs FileServer with an optional port (defaults to 8000).

```java FileClient server port filename``` specifies the remote server (IP or hostname), server port, and filename to request.

