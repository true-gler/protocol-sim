# Simulator for Anonymization Protocols

## Implementation
This software can be used to simulate anonymity network protocols.
The goal was to provide a tool to empirically analyze anonymization protocols.
One can run simulations on the pre defined network and perform attack sczenarios on the protocols in order to find possible (unknown) security issues.

## Analyzation of CROWDS
As an example-protocol, CROWDS, is implemented with Dijkstra algorithm to find the path through the network.
(The simulator was designed with respect to extend it easily by further protocols)


### Probable innocence
The starting node of any communication in the crowd has credible innocence, if:

![premise](https://raw.githubusercontent.com/true-gler/protocol-sim/master/docu/1.gif)

To achieve probable innocence, the following must apply:

![premise](https://raw.githubusercontent.com/true-gler/protocol-sim/master/docu/2.gif)


Furthermore, n denotes the number of all participants in the crowd and c the number of the collabora-
attacker at the time a transfer is started by the crowd
(the path is set at the beginning, so other attackers are negligible). From this
the core theorem of the work on the Crowds protocol is found, which has also been verified by our simulator. 

Verification of the Theorem 5.2 for anonymity from the work of Rubin et al on Crowds: "Anonymity for Web Transactions".
(http://avirubin.com/crowds.pdf)

![theorem](https://raw.githubusercontent.com/true-gler/protocol-sim/master/docu/theorem 5.2.png)


The initiator of the message has proven probable innocence against the collaborative
where n is the total number of nodes and c is the number of collaborating members.

In order to be able to empirically verify the correct functionality of the Crowds protocol,
we implement collaborating attackers in our simulator.
For the verification of the probable innocence under condition we simulated 100
runs twice.

At first the assumption of the theorem was observed and then the theorem was violated. 
If the theorem was violated, it should be possible to the attacker to determine the sender of the message.

To verify this, we ran 100 simulations with 200 nodes each. 
For the tranmission a probablitiy of p_f = 0.7 was selected. 
From this it follows, at 200 nodes that (c + 1) is less than 57.14 in order not to violate the requirement of the condition above. 
The number of collaborating nodes was set to 50, 55, 56, 57 and 58 nodes for five simulation runs.
A safe variant was chosen with 50 nodes. The correct sender was not detected a single time.
Within these Simulations, it was not possible for the attacker the sender with a probability greater than 1/2.

At 58 nodes, on the other hand, half of the 200 simulations the sender was detected. 
As a result:
We showed that when violating the condition the sender can be detected with a probability of 1/2. (Theorem 5.2. of Rubin et el)


## Documentation 

A detailed documentation can be found in: 

![Thesis](https://raw.githubusercontent.com/true-gler/protocol-sim/master/docu/Thesis.pdf)
(German version)
