
singleton TSShapeConstructor(BeachPalm01DAE)
{
   baseShape = "./BeachPalm01.DAE";
   loadLights = "0";
};

function BeachPalm01DAE::onLoad(%this)
{
   %this.addImposter("64", "6", "0", "0", "512", "1", "0");
}
