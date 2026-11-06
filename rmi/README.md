# RMI Sample

A tiny sample for Remote Method Invocation.

## Structure
- `Calculator`  is the remote interface used for communication
- `CalculatorImpl`  is the concrete class
- `RMIClient`  is client-side, making the method call
- `RMIServer`  receives the call

## RMI Notes
- The interface must extend `Remote`, and all methods must throw `RemoteException`. Arguments and return values must be serializable.
- The class implementing the remote interface must extend `UnicastRemoteObject` so it can export the object.
- Server creates the concrete class instance, starts an RMI registry on default port 1099, exports the object, and registers the service under the given name
- Client locates the service stub in the registry and invokes the methods