
singleton TSShapeConstructor(Palm02DAE)
{
   baseShape = "./Palm02.DAE";
   loadLights = "0";
};

function Palm02DAE::onLoad(%this)
{
   %this.addImposter("64", "8", "0", "0", "512", "1", "0");
}
