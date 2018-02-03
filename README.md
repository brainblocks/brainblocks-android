# brainblocks-android

Android SDK for integrating [BrainBlocks](http://BrainBlocks.io) into a mobile app

##Installation

To use the SDK in your application, follow the instructions [here](https://jitpack.io/#brainblocks/brainblocks-android/).

Be sure to use the latest version. Snapshots may not be stable.

##Usage

###Payment Functionality

```Java

Brainblock bb = Brainblock.getBrainBlock(this, "<Your XRB Payment Address Here>");

// payment rai amount. rai = 1xrb/1000000
double amount = 1000;

bb.pay_with_XRB_start(amount);


```

This will automatically generate all the dialogs necessary for directing your client through the payment process.

###Currency to XRB conversion

```Java

// first argument is currency, second is amount you wish to convert to XRB, and 3rd is a callback that returns the result
// when the response arrives
bb.convertToXRB("cad", "100", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                //do something with the result
            }
});

```

Supported currencies are: aud, brl, cad, chf, clp, cny, czk, dkk, eur, gbp, hkd, huf, idr, ils, inr, jpy, krw, mxn, myr, nok, nzd, php, pkr, pln, rub, sek, sgd, thb, try, usd, twd, zar.


Donate to me (contributor): xrb_3anuobu4qk7nrbouax4jc4mx77ye3eun541kn85ceeho415zs4j7ty1uzsfp

Donate to BrainBlocks: xrb_164xaa1ojy6qmq9e8t94mz8izr4mkf1sojb6xrmstru5jsif48g5kegcqg7y




