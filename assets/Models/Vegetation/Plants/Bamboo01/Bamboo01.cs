
singleton TSShapeConstructor(Bamboo01DAE)
{
   baseShape = "./Bamboo01.DAE";
   loadLights = "0";
};

function Bamboo01DAE::onLoad(%this)
{
   %this.addImposter("56", "6", "0", "0", "512", "1", "0");
}
