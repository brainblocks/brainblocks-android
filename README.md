# brainblocks-android

Android SDK for integrating [BrainBlocks](http://BrainBlocks.io) into a mobile app

## Installation

To use the SDK in your application, follow the instructions [here](https://jitpack.io/#brainblocks/brainblocks-android/).

Be sure to use the latest version. Snapshots may not be stable.

## Usage

### Payment Functionality

```Java

Brainblock bb = Brainblock.getBrainBlock(this, "<Your XRB Payment Address Here>");

// payment rai amount. rai = 1xrb/1000000
int amount = 1000;

bb.pay_with_XRB_start(amount);
```

You also need to give the FragmentManager of the Activity everytime it resumes, in case that it has changed (ie. because of an orientation change).

```Java
@Override
public void onResume(){
    super.onResume();

    if(bb != null){
        bb.setFragmentManager(getFragmentManager());
    }
}
```

This will automatically generate all the dialogs necessary for directing your client through the payment process. 

Here is what the [flow](https://imgur.com/a/720nb) looks like. 

### Currency to XRB conversion

```Java

// first argument is currency, second is amount you wish to convert to XRB, 
// the 3rd is a callback that returns the result when the response arrives
bb.convertToXRB("cad", "100", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                //do something with the result
            }
});
```

Supported currencies are: aud, brl, cad, chf, clp, cny, czk, dkk, eur, gbp, hkd, huf, idr, ils, inr, jpy, krw, mxn, myr, nok, nzd, php, pkr, pln, rub, sek, sgd, thb, try, usd, twd, zar.

## Donate

Donate to me (contributor): xrb_3anuobu4qk7nrbouax4jc4mx77ye3eun541kn85ceeho415zs4j7ty1uzsfp

Donate to BrainBlocks: xrb_164xaa1ojy6qmq9e8t94mz8izr4mkf1sojb6xrmstru5jsif48g5kegcqg7y




