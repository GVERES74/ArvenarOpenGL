
singleton TSShapeConstructor(Palm01DAE)
{
   baseShape = "./Palm01.DAE";
   loadLights = "0";
};

function Palm01DAE::onLoad(%this)
{
   %this.addImposter("64", "8", "0", "0", "512", "1", "0");
}
