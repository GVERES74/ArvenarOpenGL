
singleton TSShapeConstructor(CecropiaTree01DAE)
{
   baseShape = "./CecropiaTree01.DAE";
   loadLights = "0";
};

function CecropiaTree01DAE::onLoad(%this)
{
   %this.addImposter("0", "8", "0", "0", "512", "1", "0");
}
